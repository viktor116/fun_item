package com.soybeani.entity.client.renderer;

import com.soybeani.config.InitValue;
import com.soybeani.entity.custom.CreeperSkeletonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/12/9 15:55
 * @description
 */
@Environment(EnvType.CLIENT)
public class CreeperSkeletonEntityRenderer extends SkeletonEntityRenderer<CreeperSkeletonEntity> {
    private static final Identifier TEXTURE = InitValue.id("textures/entity/mob/creeper_skeleton.png");
    public CreeperSkeletonEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }
    @Override
    public Identifier getTexture(CreeperSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }
}
