package com.soybeani.entity.client.renderer;


import com.soybeani.entity.custom.MinecartEntity;
import com.soybeani.entity.custom.OakBoatEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;


public class OakBoatEntityRenderer extends EntityRenderer<OakBoatEntity>  {

    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/boat/oak.png");
    protected final BoatEntityModel model;

    public OakBoatEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new BoatEntityModel(ctx.getPart(EntityModelLayers.createBoat(BoatEntity.Type.OAK)));
        this.shadowRadius = 0.8F;
    }


    @Override
    public void render(OakBoatEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        
        if(entity.isBaby()) {
            matrices.scale(.6f, .6f, .6f);
        }
        
        matrices.translate(0.0F, 0.375F, 0.0F);
        
        float interpolatedYaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevYaw, entity.getYaw());
        
        float pitch = 0;
        float roll = 0;
        if (entity.getVelocity().length() > 0.1) {
            pitch = -1.0f * (float)entity.getVelocity().length();
        }
        
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F - interpolatedYaw));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F + pitch));

        float time = (float)entity.age + tickDelta;
        float speed = (float)Math.sqrt(
            entity.getVelocity().x * entity.getVelocity().x + 
            entity.getVelocity().z * entity.getVelocity().z
        );
        float paddleAnimation = speed > 0.01f ? MathHelper.sin(time * speed * 0.5F) * 0.3F : 0.0F;

        BoatEntity dummyBoat = new BoatEntity(EntityType.BOAT, entity.getWorld());
        dummyBoat.setPosition(entity.getPos());
        this.model.setAngles(dummyBoat, tickDelta, 0.0F, paddleAnimation, 0.0F, 0.0F);

        int overlay = OverlayTexture.DEFAULT_UV;
        if (entity.getHurtTime() > 0) {
            overlay = OverlayTexture.packUv(OverlayTexture.getU(0.0F), OverlayTexture.getV(true));
        }

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity)));
        this.model.render(matrices, vertexConsumer, light, overlay);

        if (!entity.isSubmergedInWater() && this.model instanceof ModelWithWaterPatch) {
            ModelWithWaterPatch modelWithWaterPatch = (ModelWithWaterPatch)this.model;
            VertexConsumer waterMaskConsumer = vertexConsumers.getBuffer(RenderLayer.getWaterMask());
            modelWithWaterPatch.getWaterPatch().render(matrices, waterMaskConsumer, light, OverlayTexture.DEFAULT_UV);
        }

        matrices.pop();
    }

    @Override
    public Identifier getTexture(OakBoatEntity entity) {
        return TEXTURE;
    }

}
