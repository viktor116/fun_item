package com.soybeani.entity.vehicle;

import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.entity.EntityRegisterClient;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class FlyBoatEntity extends BoatEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final Integer IN_FLY = 1;
    private static final Integer CONSTANT_FLY = 2;
    private Integer fly = 0;
    private boolean accelerate = false;

    public FlyBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public FlyBoatEntity(World world, double x, double y, double z) {
        this(EntityRegister.FLY_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    public void tick() {
        Vec3d velocity = this.getVelocity();
        Vec3d direction = this.getRotationVector().normalize();
        float factor = 0.5f;
        if (getFly() == IN_FLY) { //往上飞行
            if(getAccelerate()){
                this.setVelocity(velocity.x, velocity.y + factor , velocity.z);
            } else if (velocity.getY() < 1) {
                this.setVelocity(velocity.x, velocity.y + 0.1, velocity.z);
            }
        } else if (getFly() == CONSTANT_FLY) { //恒定飞行
            if(getAccelerate()){
                this.setVelocity(velocity.x + direction.x * factor, this.getGravity(), velocity.z + direction.z * factor);
            }else{
                this.setVelocity(velocity.x, this.getGravity(), velocity.z);
            }
        } else { //不飞
            if(getAccelerate()){
                this.setVelocity(velocity.x + direction.x * factor, velocity.y, velocity.z + direction.z *factor);
            }else{
                this.setVelocity(velocity.x, velocity.y, velocity.z);
            }
        }
        super.tick();
    }

    @Override
    public Item asItem() {
        return ItemsRegister.FLY_BOAT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Environment(EnvType.CLIENT)
    private PlayState predicate(AnimationState<GeoAnimatable> animationState) {
        if (!this.getWorld().isClient()) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // 获取当前客户端的玩家
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity clientPlayer = client.player;

        // 获取船的驾驶员
        Entity driver = this.getControllingPassenger();

        // 如果没有驾驶员，或者当前客户端玩家不是驾驶员，播放静止动画
        if (driver == null || clientPlayer == null) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // 只有当客户端玩家是驾驶员时才检查按键状态
        boolean pressingForward = client.options.forwardKey.isPressed();
        boolean pressingLeft = client.options.leftKey.isPressed();
        boolean pressingRight = client.options.rightKey.isPressed();

        if (getFly() == IN_FLY) {
            if (getAccelerate()) {
                animationState.getController().setAnimation(RawAnimation.begin()
                        .then("animation.accelerate_fly", Animation.LoopType.LOOP));
            } else {
                animationState.getController().setAnimation(RawAnimation.begin()
                        .then("animation.fly", Animation.LoopType.LOOP));
            }
        } else if (getAccelerate()) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.accelerate", Animation.LoopType.LOOP));
        } else if (pressingForward) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.move", Animation.LoopType.LOOP));
        } else if (pressingLeft) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.turnleft", Animation.LoopType.LOOP));
        } else if (pressingRight) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.turnright", Animation.LoopType.LOOP));
        } else {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.idle", Animation.LoopType.LOOP));
        }

        return PlayState.CONTINUE;
    }

    public void SwitchFly() {
        this.fly++;
        if (this.fly > 2) {
            this.fly = 0;
        }
    }

    public Integer getFly() {
        return this.fly;
    }

    public void setAccelerate(boolean b) {
        this.accelerate = b;
    }

    public boolean getAccelerate() {
        return this.accelerate;
    }
}
