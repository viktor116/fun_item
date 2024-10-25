package com.soybeani.entity.client.model;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import com.soybeani.entity.vehicle.Su7CarEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2024/10/24 15:35
 * @description
 */
public class Ice2BoatEntityModel extends GeoModel<Ice2BoatEntity> {

    public static Ice2BoatEntityModel INSTANCE = new Ice2BoatEntityModel();

    @Override
    public Identifier getModelResource(Ice2BoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"geo/ice2_boat.geo.json");
    }

    @Override
    public Identifier getTextureResource(Ice2BoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/boat/ice2_boat.png");
    }

    @Override
    public Identifier getAnimationResource(Ice2BoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"animations/ice2_boat.animation.json");
    }

    @Override
    public void setCustomAnimations(Ice2BoatEntity animatable, long instanceId, AnimationState<Ice2BoatEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }

}
