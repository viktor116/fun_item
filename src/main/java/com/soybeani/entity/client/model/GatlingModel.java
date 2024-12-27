package com.soybeani.entity.client.model;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.FlyBoatEntity;
import com.soybeani.items.item.GatlingItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2024/12/27 15:04
 * @description
 */
public class GatlingModel  extends GeoModel<GatlingItem> {
    @Override
    public Identifier getModelResource(GatlingItem animatable) {
        return Identifier.of(InitValue.MOD_ID,"geo/gatling_gun.geo.json");
    }

    @Override
    public Identifier getTextureResource(GatlingItem animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/gatling_gun.png");
    }

    @Override
    public Identifier getAnimationResource(GatlingItem animatable) {
        return Identifier.of(InitValue.MOD_ID,"animations/gatling_gun.animation.json");
    }
}
