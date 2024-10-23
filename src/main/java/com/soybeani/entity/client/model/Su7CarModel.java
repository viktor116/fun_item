package com.soybeani.entity.client.model;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.Su7CarEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2024/10/19 12:00
 * @description
 */
public class Su7CarModel extends GeoModel<Su7CarEntity> {

    @Override
    public Identifier getModelResource(Su7CarEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"geo/xiaomisu7.geo.json");
    }

    @Override
    public Identifier getTextureResource(Su7CarEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/xiaomisu7.png");
    }

    @Override
    public Identifier getAnimationResource(Su7CarEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"animations/xiaomisu7.animation.json");
    }

    @Override
    public void setCustomAnimations(Su7CarEntity animatable, long instanceId, AnimationState<Su7CarEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
