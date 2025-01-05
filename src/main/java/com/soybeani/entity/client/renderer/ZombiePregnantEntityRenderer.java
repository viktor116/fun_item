package com.soybeani.entity.client.renderer;

import com.soybeani.config.InitValue;
import com.soybeani.entity.custom.SkeletonZombieEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/12/9 15:22
 * @description
 */
@Environment(EnvType.CLIENT)
public class ZombiePregnantEntityRenderer extends ZombieBaseEntityRenderer<SkeletonZombieEntity, ZombieEntityModel<SkeletonZombieEntity>> {
    private static final Identifier TEXTURE = InitValue.id("textures/entity/mob/skeleton_zombie.png");
    public ZombiePregnantEntityRenderer(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.ZOMBIE, EntityModelLayers.ZOMBIE_INNER_ARMOR, EntityModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public ZombiePregnantEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(ctx, new ZombieEntityModel(ctx.getPart(layer)), new ZombieEntityModel(ctx.getPart(legsArmorLayer)), new ZombieEntityModel(ctx.getPart(bodyArmorLayer)));
    }

    @Override
    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }
}