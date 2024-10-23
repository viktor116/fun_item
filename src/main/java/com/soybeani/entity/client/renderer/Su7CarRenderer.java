package com.soybeani.entity.client.renderer;

import com.soybeani.config.InitValue;
import com.soybeani.entity.client.model.Su7CarModel;
import com.soybeani.entity.vehicle.Su7CarEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * @author soybean
 * @date 2024/10/19 12:10
 * @description
 */
public class Su7CarRenderer extends GeoEntityRenderer<Su7CarEntity> {

    public Su7CarRenderer(net.minecraft.client.render.entity.EntityRendererFactory.Context renderManager) {
        super(renderManager, new Su7CarModel());
    }

    @Override
    public Identifier getTexture(Su7CarEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/xiaomisu7.png");
    }

    @Override
    public void render(Su7CarEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if(entity.isBaby()){
            poseStack.scale(.4f,.4f,.4f);
        }
        poseStack.scale(1.5f,1.5f,1.5f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
