package com.soybeani.entity.vehicle;

import com.soybeani.entity.EntityRegister;
import com.soybeani.network.packet.BlockPosPayload;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
    private static final double BASE_SPEED = 0.8;
    private static final float MAX_DAMAGE = 40.0f;
    private static final float ROTATION_SPEED = 2.0f;
    private static final float ACCELERATION = 0.04F;
    private static final float DECELERATION = 0.02F;
    private static final float MAX_FORWARD_SPEED = 1.5F;

    // DataTracker fields
    private static final TrackedData<Float> FORWARD_SPEED = DataTracker.registerData(FlyingWoodSwordEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> YAW_VELOCITY = DataTracker.registerData(FlyingWoodSwordEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> LEFT_MOVING = DataTracker.registerData(FlyingWoodSwordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> RIGHT_MOVING = DataTracker.registerData(FlyingWoodSwordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

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
        builder.add(FORWARD_SPEED, 0.0f);
        builder.add(YAW_VELOCITY, 0.0f);
        builder.add(LEFT_MOVING, false);
        builder.add(RIGHT_MOVING, false);
    }

    @Override
    public void tick() {
        super.tick();

        // 如果有驾驶员且在客户端
        if (this.pilot != null && !this.pilot.isRemoved() && this.pilot.getVehicle() == this) {
            if (this.getWorld().isClient) {
                this.updateMovement();
            }

            // 应用移动
            Vec3d movement = this.calculateMovement();
            this.setVelocity(movement);
            this.move(MovementType.SELF, this.getVelocity());

            // 更新DataTracker
            this.dataTracker.set(FORWARD_SPEED, (float)movement.length());

            // 水中上浮
            if (this.isTouchingWater()) {
                this.addVelocity(0, 0.1, 0);
            }
        } else {
            // 没有驾驶员时停止
            this.setVelocity(Vec3d.ZERO);
            this.dataTracker.set(FORWARD_SPEED, 0.0f);
            this.yawVelocity = 0.0f;
        }

        // 应用转向
        this.setYaw(this.getYaw() + this.yawVelocity);
    }

    private void updateMovement() {
        float forwardSpeed = this.dataTracker.get(FORWARD_SPEED);

        // 处理转向
        if (this.pressingLeft) {
            this.yawVelocity -= ROTATION_SPEED;
        }
        if (this.pressingRight) {
            this.yawVelocity += ROTATION_SPEED;
        }

        // 处理前进/后退
        if (this.pressingForward) {
            forwardSpeed = Math.min(forwardSpeed + ACCELERATION, MAX_FORWARD_SPEED);
        } else if (this.pressingBack) {
            forwardSpeed = Math.max(forwardSpeed - ACCELERATION, -MAX_FORWARD_SPEED * 0.5f);
        } else {
            forwardSpeed *= (1 - DECELERATION);
        }

        // 更新速度和动画状态
        this.dataTracker.set(FORWARD_SPEED, forwardSpeed);
        this.dataTracker.set(LEFT_MOVING, this.pressingLeft);
        this.dataTracker.set(RIGHT_MOVING, this.pressingRight);
        if(pilot instanceof ServerPlayerEntity serverPlayer){
            ServerPlayNetworking.send(serverPlayer,new BlockPosPayload(pilot.getBlockPos()));
        }
        // 应用阻力
        this.yawVelocity *= this.velocityDecay;
    }

    private Vec3d calculateMovement() {
        float forwardSpeed = this.dataTracker.get(FORWARD_SPEED);
        float yaw = this.getYaw();

        // 根据朝向和速度计算移动向量
        double vx = -MathHelper.sin(yaw * 0.017453292F) * forwardSpeed;
        double vz = MathHelper.cos(yaw * 0.017453292F) * forwardSpeed;

        return new Vec3d(vx, 0, vz);
    }

    public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack,boolean pressingJump,boolean pressingSneak) {
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
            this.dataTracker.set(FORWARD_SPEED, nbt.getFloat("ForwardSpeed"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("ForwardSpeed", this.dataTracker.get(FORWARD_SPEED));
    }

    @Override
    public void onPassengerLookAround(Entity passenger) {
        super.onPassengerLookAround(passenger);
        if (passenger instanceof LivingEntity) {
            this.pilot = (LivingEntity) passenger;
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }

        // 调用父类的damage方法来处理基础伤害逻辑
        boolean damaged = super.damage(source, amount);

        if (!this.getWorld().isClient && !this.isRemoved()) {
            if (this.getDamageWobbleStrength() > MAX_DAMAGE) {
                this.killAndDropSelf(source);
            }
        }
        return damaged;
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
                return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }
}
