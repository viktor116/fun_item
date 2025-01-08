package com.soybeani.entity.client.renderer;

import com.soybeani.config.InitValue;
import com.soybeani.entity.client.ModEntityModelLayers;
import com.soybeani.entity.client.model.ZombiePregnantEntityModel;
import com.soybeani.entity.custom.SkeletonZombieEntity;
import com.soybeani.entity.custom.ZombiePregnantEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/12/9 15:22
 * @description
 */
@Environment(EnvType.CLIENT)
public class ZombiePregnantEntityRenderer extends MobEntityRenderer<ZombiePregnantEntity,ZombiePregnantEntityModel> {
    public ZombiePregnantEntityRenderer(EntityRendererFactory.Context context) {
        this(context, ModEntityModelLayers.ZOMBIE_PREGNANT, EntityModelLayers.ZOMBIE_INNER_ARMOR, EntityModelLayers.ZOMBIE_OUTER_ARMOR);
    }
    @Override
    public Identifier getTexture(ZombiePregnantEntity entity) {
        return Identifier.of(InitValue.MOD_ID, "textures/entity/mob/zombie_pregnant.png");
    }

    public ZombiePregnantEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(ctx, new ZombiePregnantEntityModel(ctx.getPart(layer)), 0.5f);
    }

}