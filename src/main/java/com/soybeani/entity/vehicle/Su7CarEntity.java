package com.soybeani.entity.vehicle;

import com.soybeani.entity.EntityRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;

import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.dataticket.SerializableDataTicket;


/**
 * @author soybean
 * @date 2024/10/19 10:33
 * @description
 */
public class Su7CarEntity extends HorseEntity implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public Su7CarEntity(EntityType<? extends HorseEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.25)
                .add(EntityAttributes.GENERIC_JUMP_STRENGTH, 0.0);  // 禁用跳跃
    }

    @Override
    public boolean isSaddled() {
        return true;  // 总是可以骑乘，不需要鞍
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controller) {
        controller.add(new AnimationController<GeoAnimatable>(this,"controller", 0, this::predicate));
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        // 可以自定义乘客位置
    }


    private PlayState predicate(AnimationState<GeoAnimatable> animationState) {
        if(animationState.isMoving()){
            animationState.getController().setAnimation(RawAnimation.begin().then("animation.move",Animation.LoopType.LOOP));
        }
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.idle",Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getBoneResetTime() {
        return GeoEntity.super.getBoneResetTime();
    }

    @Override
    public boolean shouldPlayAnimsWhileGamePaused() {
        return GeoEntity.super.shouldPlayAnimsWhileGamePaused();
    }

    @Override
    public <D> @Nullable D getAnimData(SerializableDataTicket<D> dataTicket) {
        return GeoEntity.super.getAnimData(dataTicket);
    }

    @Override
    public <D> void setAnimData(SerializableDataTicket<D> dataTicket, D data) {
        GeoEntity.super.setAnimData(dataTicket, data);
    }

    @Override
    public void triggerAnim(@Nullable String controllerName, String animName) {
        GeoEntity.super.triggerAnim(controllerName, animName);
    }

    @Override
    public double getTick(Object entity) {
        return GeoEntity.super.getTick(entity);
    }

    @Override
    public AnimatableInstanceCache animatableCacheOverride() {
        return GeoEntity.super.animatableCacheOverride();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EntityRegister.SU7.create(world);
    }
}
