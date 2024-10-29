package com.soybeani.entity.client.model;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.FlyBoatEntity;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2024/10/24 15:35
 * @description
 */
public class FlyBoatEntityModel extends GeoModel<FlyBoatEntity> {

    public static FlyBoatEntityModel INSTANCE = new FlyBoatEntityModel();

    @Override
    public Identifier getModelResource(FlyBoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"geo/fly_boat.geo.json");
    }

    @Override
    public Identifier getTextureResource(FlyBoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/boat/fly_boat.png");
    }

    @Override
    public Identifier getAnimationResource(FlyBoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"animations/fly_boat.animation.json");
    }

    @Override
    public void setCustomAnimations(FlyBoatEntity animatable, long instanceId, AnimationState<FlyBoatEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }

}
