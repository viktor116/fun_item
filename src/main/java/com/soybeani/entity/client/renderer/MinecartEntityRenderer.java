package com.soybeani.entity.client.renderer;


import com.soybeani.entity.custom.MinecartEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;


public class MinecartEntityRenderer extends EntityRenderer<MinecartEntity>  {

    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/minecart.png");
    protected final EntityModel<Entity> model;

    public MinecartEntityRenderer(EntityRendererFactory.Context ctx) {
//        super(ctx,new MinecartEntityModel(ctx.getPart(EntityModelLayers.MINECART)),1F);
        super(ctx);
        this.model = new MinecartEntityModel(ctx.getPart(EntityModelLayers.MINECART));
    }

    @Override
    public Identifier getTexture(MinecartEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(MinecartEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        
        if(entity.isBaby()) {
            matrices.scale(.6f, .6f, .6f);
        }
        
        matrices.translate(0.0F, 0.375F, 0.0F);
        
        float interpolatedYaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevYaw, entity.getYaw());
        
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F - interpolatedYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));

        int overlay = OverlayTexture.DEFAULT_UV;
        if (entity.getHurtTime() > 0) {
            overlay = OverlayTexture.packUv(OverlayTexture.getU(0.0F), OverlayTexture.getV(true));
        }

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity)));
        this.model.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
    }
}
