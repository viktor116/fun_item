package com.soybeani.entity.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.soybeani.config.InitValue;
import com.soybeani.entity.client.model.FlyBoatEntityModel;
import com.soybeani.entity.vehicle.FlyBoatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author soybean
 * @date 2024/10/19 12:10
 * @description
 */
@Environment(EnvType.CLIENT)
public class FlyBoatEntityRenderer extends GeoEntityRenderer<FlyBoatEntity> {

    private static final FlyBoatEntityModel flyBoatEntityModel = new FlyBoatEntityModel();
    private static final Identifier CUSTOM_BOAT_TEXTURE = Identifier.of(InitValue.MOD_ID +":textures/entity/boat/fly_boat.png");
    private final Map<BoatEntity.Type, Pair<Identifier, CompositeEntityModel<BoatEntity>>> texturesAndModels;

    public FlyBoatEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, flyBoatEntityModel);
        this.texturesAndModels = Stream.of(BoatEntity.Type.values())
                .collect(ImmutableMap.toImmutableMap(
                        type -> type,
                        type -> Pair.of(CUSTOM_BOAT_TEXTURE, this.createModel(renderManager, type, false))
                ));
    }

    private CompositeEntityModel<BoatEntity> createModel(EntityRendererFactory.Context ctx, BoatEntity.Type type, boolean chest) {
        EntityModelLayer entityModelLayer = EntityModelLayers.createBoat(type);
        ModelPart modelPart = ctx.getPart(entityModelLayer);
        return type == BoatEntity.Type.BAMBOO
                ? new RaftEntityModel(modelPart)
                : new BoatEntityModel(modelPart);
    }

    @Override
    public Identifier getTexture(FlyBoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/boat/fly_boat.png");
    }

    @Override
    public void render(FlyBoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0F, 0.0F, 0.0F);
        // 获取插值后的角度，使运动更平滑
        float yaw = boatEntity.getYaw(g);
        float pitch = boatEntity.getPitch(g);
        float roll = 0.0F;

        // 按照正确的顺序应用旋转
        // 1. 先处理偏航角(Yaw)
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(360.0F - yaw));
        // 2. 然后是俯仰角(Pitch)
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        // 3. 最后是翻滚角(Roll)，如果需要的话
        if (roll != 0.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
        }
        super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);

        matrixStack.translate(0.0F, 0.375F, 0.0F);

        Pair<Identifier, CompositeEntityModel<BoatEntity>> pair = this.texturesAndModels.get(boatEntity.getVariant());
        CompositeEntityModel<BoatEntity> compositeEntityModel = pair.getSecond();

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        compositeEntityModel.setAngles(boatEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);

        if (!boatEntity.isSubmergedInWater()) {
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
            if (compositeEntityModel instanceof ModelWithWaterPatch) {
                ModelWithWaterPatch modelWithWaterPatch = (ModelWithWaterPatch)compositeEntityModel;
                modelWithWaterPatch.getWaterPatch().render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
            }
        }

        matrixStack.pop();
    }


}
