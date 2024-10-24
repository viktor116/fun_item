package com.soybeani.entity.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.soybeani.config.InitValue;
import com.soybeani.entity.client.model.Ice2BoatEntityModel;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author soybean
 * @date 2024/10/19 12:10
 * @description
 */
public class Ice2BoatEntityRenderer extends GeoEntityRenderer<Ice2BoatEntity> {

    public Ice2BoatEntityRenderer(net.minecraft.client.render.entity.EntityRendererFactory.Context renderManager) {
        super(renderManager, new Ice2BoatEntityModel());
    }

    @Override
    public Identifier getTexture(Ice2BoatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/boat/ice2_boat.png");
    }

    @Override
    public void render(Ice2BoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        // 获取插值后的角度，使运动更平滑
        float yaw = boatEntity.getYaw(g);
        float pitch = boatEntity.getPitch(g);
        float roll = 0.0F;

        // 按照正确的顺序应用旋转
        // 1. 先处理偏航角(Yaw)
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));
        // 2. 然后是俯仰角(Pitch)
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        // 3. 最后是翻滚角(Roll)，如果需要的话
        if (roll != 0.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
        }
        // 调用父类的渲染方法
        super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);

        matrixStack.pop();
    }
}
