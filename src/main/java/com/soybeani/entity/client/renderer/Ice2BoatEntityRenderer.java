package com.soybeani.entity.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.soybeani.config.InitValue;
import com.soybeani.entity.client.model.Ice2BoatEntityModel;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author soybean
 * @date 2024/10/19 12:10
 * @description
 */
public class Ice2BoatEntityRenderer extends GeoEntityRenderer<Ice2BoatEntity> {

    private static final Ice2BoatEntityModel ice2BoatEntityModel = new Ice2BoatEntityModel();

    public Ice2BoatEntityRenderer(net.minecraft.client.render.entity.EntityRendererFactory.Context renderManager) {
        super(renderManager, ice2BoatEntityModel);
    }

    @Override
    public Identifier getTexture(Ice2BoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/boat/ice2_boat.png");
    }

    @Override
    public void render(Ice2BoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
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

        Optional<GeoBone> waterPatch = this.getGeoModel().getBone("water_patch");
        // 如果找到水面遮罩骨骼，单独渲染它来抵消水的效果
        if (waterPatch.isPresent() && boatEntity.isSubmergedInWater()) {
            GeoBone waterBone = waterPatch.get();
            if (!waterBone.isHidden()) {
                // 使用遮罩渲染层
                VertexConsumer waterMaskConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());

                // 应用骨骼变换
                matrixStack.translate(
                        waterBone.getPivotX() / 16.0f,
                        waterBone.getPivotY() / 16.0f,
                        waterBone.getPivotZ() / 16.0f
                );

                // 应用骨骼旋转
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation(waterBone.getRotZ()));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(waterBone.getRotY()));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(waterBone.getRotX()));

                // 渲染遮罩
                Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
                Matrix3f matrix3f = matrixStack.peek().getNormalMatrix();

                // 渲染一个与船体内部形状匹配的遮罩平面
                waterMaskConsumer.vertex(matrix4f, -1.0F, -1.0F, 0.0F)
                        .color(0.8F, 0.8F, 0.8F, 1.0F)
                        .texture(0.0F, 0.0F)
                        .overlay(OverlayTexture.DEFAULT_UV)
                        .light(i)
                        .normal(0.0F, 1.0F, 0.0F);

                waterMaskConsumer.vertex(matrix4f, 1.0F, -1.0F, 0.0F)
                        .color(0.8F, 0.8F, 0.8F, 1.0F)
                        .texture(1.0F, 0.0F)
                        .overlay(OverlayTexture.DEFAULT_UV)
                        .light(i)
                        .normal(0.0F, 1.0F, 0.0F);

                waterMaskConsumer.vertex(matrix4f, 1.0F, 1.0F, 0.0F)
                        .color(0.8F, 0.8F, 0.8F, 1.0F)
                        .texture(1.0F, 1.0F)
                        .overlay(OverlayTexture.DEFAULT_UV)
                        .light(i)
                        .normal(0.0F, 1.0F, 0.0F);

                waterMaskConsumer.vertex(matrix4f, -1.0F, 1.0F, 0.0F)
                        .color(0.8F, 0.8F, 0.8F, 1.0F)
                        .texture(0.0F, 1.0F)
                        .overlay(OverlayTexture.DEFAULT_UV)
                        .light(i)
                        .normal(0.0F, 1.0F, 0.0F);

            }
        }
        super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);

        matrixStack.pop();
    }


}
