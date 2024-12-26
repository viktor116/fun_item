package com.soybeani.entity.vehicle;

import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.network.packet.BlockPosPayload;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.payload.PayloadHelper;
import net.fabricmc.fabric.impl.registry.sync.packet.DirectRegistryPacketHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/26 11:19
 * @description 飞行木剑载具实体
 */
public class FlyingWoodSwordEntity extends VehicleEntity {
    private static final float MAX_DAMAGE = 40.0f;
    private static final float ROTATION_SPEED = 2.0f;
    // Input fields
    private boolean pressingLeft;
    private boolean pressingRight;
    private boolean pressingForward;
    private boolean pressingBack;
    private boolean pressingJump;
    private boolean pressingSneak;

    // Physics fields
    private float velocityDecay = 0.9F;
    private float yawVelocity;
    private LivingEntity pilot;
    private Vec3d lastLocation;

    public FlyingWoodSwordEntity(EntityType<? extends VehicleEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public FlyingWoodSwordEntity(World world, double x, double y, double z) {
        this(EntityRegister.FLYING_WOOD_SWORD_ENTITY_ENTITY_TYPE, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    protected Entity.MoveEffect getMoveEffect() {
        return MoveEffect.EVENTS;
    }

    @Override
    public void tick() {
        this.lastLocation = this.getPos();
        if (this.pilot != null && !this.pilot.isRemoved() && this.pilot.getVehicle() == this) {
            InitValue.LOGGER.info("this.isLogicalSideForUpdatingMovement()="+this.isLogicalSideForUpdatingMovement());
            if (this.isLogicalSideForUpdatingMovement()) {
                Vec3d movement = this.calculateMovement();
                this.setVelocity(movement);
                InitValue.LOGGER.info("movement数值:{},x={}", movement, movement.x);
                this.move(MovementType.SELF, this.getVelocity());
            } else {
                // 非逻辑端时设置为零向量
                this.setVelocity(Vec3d.ZERO);
            }
        }
        this.setPitch(0);
        // 应用转向
        this.setYaw(this.getYaw() + this.yawVelocity);
        super.tick();
    }

    private Vec3d calculateMovement() {
        if (this.pilot == null || this.pilot.isRemoved()) {
            return Vec3d.ZERO;
        }

        // 获取驾驶员的朝向
        float yaw = this.pilot.getYaw();  // Minecraft中，0度是南方，90度是西方
        float pitch = this.pilot.getPitch();

        // 基础移动速度
        double speed = 1D;
        Vec3d movement = Vec3d.ZERO;

        // 只处理前进键，根据玩家视角移动
        if (this.pressingForward) {
            // 计算水平方向的移动
            double horizontalSpeed = Math.cos(Math.toRadians(pitch)) * speed;
            // 修正南北方向
            double x = -Math.sin(Math.toRadians(yaw)) * horizontalSpeed;
            double z = Math.cos(Math.toRadians(yaw)) * horizontalSpeed;  // 去掉负号

            // 计算垂直方向的移动（基于pitch）
            double y = -Math.sin(Math.toRadians(pitch)) * speed;

            movement = new Vec3d(x, y, z);
        }

        return movement;
    }

    private void updateMovement() {
        // 处理转向
        if (this.pressingLeft) {
            this.yawVelocity -= ROTATION_SPEED;
        }
        if (this.pressingRight) {
            this.yawVelocity += ROTATION_SPEED;
        }
        if (pilot instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new BlockPosPayload(pilot.getBlockPos()));
        }
        // 应用阻力
        this.yawVelocity *= this.velocityDecay;
    }


    public void setInputs(boolean pressingForward, boolean pressingBack, boolean pressingLeft, boolean pressingRight, boolean pressingJump, boolean pressingSneak) {
        this.pressingLeft = pressingLeft;
        this.pressingRight = pressingRight;
        this.pressingForward = pressingForward;
        this.pressingBack = pressingBack;
        this.pressingJump = pressingJump;
        this.pressingSneak = pressingSneak;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("ForwardSpeed")) {
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public void onPassengerLookAround(Entity passenger) {
        super.onPassengerLookAround(passenger);
        if (passenger instanceof LivingEntity) {
            this.pilot = (LivingEntity) passenger;
        }
    }
    @Override
    public LivingEntity getControllingPassenger() {
        return pilot;
    }

    @Override
    public Item asItem() {
        return Items.WOODEN_SWORD;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        }

        if (!this.getWorld().isClient) {
            if (!player.isSneaking()) {
                if(player.startRiding(this)){
                    player.setPos(this.prevX,this.prevY,this.prevZ);
                    return ActionResult.CONSUME;
                }else {
                    return ActionResult.PASS;
                }
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }
}
