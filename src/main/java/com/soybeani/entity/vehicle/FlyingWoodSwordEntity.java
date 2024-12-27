package com.soybeani.entity.vehicle;

import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author soybean
 * @date 2024/12/26 11:19
 * @description 飞行木剑载具实体
 */
public class FlyingWoodSwordEntity extends VehicleEntity implements Leashable {
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
    private Leashable.LeashData leashData;


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
            this.updateMovement();
            if (this.isLogicalSideForUpdatingMovement()) {
                Vec3d movement = this.calculateMovement();
                this.setVelocity(movement);
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

        // 基础移动速度
        double speed = 1D;
        Vec3d movement = Vec3d.ZERO;

        // 使用驾驶员的yaw来设置实体的朝向
        float playerYaw = this.pilot.getYaw();
        // 设置实体的yaw为玩家的yaw
        this.setYaw(playerYaw);

        float pitch = this.pilot.getPitch();

        // 处理前后移动
        if (this.pressingForward || this.pressingBack) {
            // 计算水平方向的移动
            double horizontalSpeed = Math.cos(Math.toRadians(pitch)) * speed;
            // 根据玩家的视角计算移动方向
            double x = -Math.sin(Math.toRadians(playerYaw)) * horizontalSpeed;
            double z = Math.cos(Math.toRadians(playerYaw)) * horizontalSpeed;
            // 计算垂直方向的移动（基于pitch）
            double y = -Math.sin(Math.toRadians(pitch)) * speed;

            // 如果是后退，反转方向
            if (this.pressingBack) {
                x = -x;
                z = -z;
                y = -y;
            }

            movement = new Vec3d(x, y, z);
        }

        // 处理左右移动
        if (this.pressingLeft || this.pressingRight) {
            // 相对于玩家视角的横向移动
            double x = Math.cos(Math.toRadians(playerYaw)) * speed;
            double z = Math.sin(Math.toRadians(playerYaw)) * speed;

            if (this.pressingRight) {
                x = -x;
                z = -z;
            }

            // 如果已经有前后移动，则合并移动向量
            if (!movement.equals(Vec3d.ZERO)) {
                movement = movement.add(new Vec3d(x, 0, z)).multiply(0.7071);
            } else {
                movement = new Vec3d(x, 0, z);
            }
        }
        if(this.pressingJump){
            movement = movement.add(0,0.5,0);
        }


        return movement;
    }

    private void updateMovement() {
        // 处理转向
        if (this.pressingLeft) {
            this.yawVelocity = -ROTATION_SPEED;
            this.setYaw(this.getYaw() + this.yawVelocity);  // 直接更新yaw
        }
        if (this.pressingRight) {
            this.yawVelocity = ROTATION_SPEED;
            this.setYaw(this.getYaw() + this.yawVelocity);  // 直接更新yaw
        }
        if (!this.pressingLeft && !this.pressingRight) {
            this.yawVelocity = 0;  // 当没有按键时重置转向速度
        }
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
        this.leashData = this.readLeashDataFromNbt(nbt);
        boolean hasPlayerUUID = nbt.contains("PlayerUUID");
        if(hasPlayerUUID){
            InitValue.LOGGER.info("PlayerUUID: " + nbt.getUuid("PlayerUUID"));
            UUID playerUUID = nbt.getUuid("PlayerUUID");
            this.pilot = this.getWorld().getPlayerByUuid(playerUUID);
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        this.writeLeashDataToNbt(nbt, this.leashData);
        if(pilot!=null){
            nbt.putUuid("PlayerUUID", this.pilot.getUuid());
        }else{
            nbt.remove("PlayerUUID");
        }
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
        ActionResult actionResult = super.interact(player, hand);
        if (actionResult != ActionResult.PASS) {
            return actionResult;
        } else if (player.shouldCancelInteraction()) {
            this.pilot = null;
            return ActionResult.PASS;
        } else if (!this.getWorld().isClient) {
            this.pilot = player;
            return player.startRiding(this,true) ? ActionResult.CONSUME : ActionResult.PASS;
        } else {
            return ActionResult.PASS;
        }
    }

    protected float getPassengerHorizontalOffset() {
        return 0.0F;
    }

    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        float f = this.getPassengerHorizontalOffset();
        if (this.getPassengerList().size() > 1) {
            int i = this.getPassengerList().indexOf(passenger);
            if (i == 0) {
                f = 0.2F;
            } else {
                f = -0.6F;
            }

            if (passenger instanceof AnimalEntity) {
                f += 0.2F;
            }
        }

        return (new Vec3d(0.0, (double) (dimensions.height() * 0.8888889F), (double) f)).rotateY(-this.getYaw() * 0.017453292F);
    }

    protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        if (!passenger.getType().isIn(EntityTypeTags.CAN_TURN_IN_BOATS)) {
            passenger.setYaw(passenger.getYaw() + this.yawVelocity);
            passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
            this.clampPassengerYaw(passenger);
            if (passenger instanceof AnimalEntity && this.getPassengerList().size() == this.getMaxPassengers()) {
                int i = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setBodyYaw(((AnimalEntity) passenger).bodyYaw + (float) i);
                passenger.setHeadYaw(passenger.getHeadYaw() + (float) i);
            }

        }
    }

    public void remove(Entity.RemovalReason reason) {
        if (!this.getWorld().isClient && reason.shouldDestroy()) {
            this.detachLeash(true, true);
        }

        super.remove(reason);
    }

    @Nullable
    @Override
    public Leashable.LeashData getLeashData() {
        return this.leashData;
    }

    @Override
    public void setLeashData(@Nullable Leashable.LeashData leashData) {
        this.leashData = leashData;
    }

    public void detachLeash(boolean sendPacket, boolean dropItem) {
        this.detachLeash((Entity) this, sendPacket, dropItem);
    }

    private static <E extends Entity> void detachLeash(E entity, boolean sendPacket, boolean dropItem) {
        if (sendPacket) {
            World var5 = entity.getWorld();
            if (var5 instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) var5;
                serverWorld.getChunkManager().sendToOtherNearbyPlayers(entity, new EntityAttachS2CPacket(entity, (Entity) null));
            }

        }
    }

    protected Text getDefaultName() {
        return Text.translatable(this.asItem().getTranslationKey());
    }

    public ItemStack getPickBlockStack() {
        return new ItemStack(this.asItem());
    }

    protected void clampPassengerYaw(Entity passenger) {
        passenger.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(passenger.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        passenger.prevYaw += g - f;
        passenger.setYaw(passenger.getYaw() + g - f);
        passenger.setHeadYaw(passenger.getYaw());
    }

    protected int getMaxPassengers() {
        return 2;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }
}
