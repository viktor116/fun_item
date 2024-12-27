package com.soybeani.mixin;

import com.soybeani.entity.vehicle.FlyingWoodSwordEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author soybean
 * @date 2024/12/27 13:46
 * @description
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {


    @Shadow @Final
    protected M model;

    @Inject(method = "render", at = @At("RETURN"))
    private void modifyRidingPose(T livingEntity, float f, float g, MatrixStack matrixStack,
                                  VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
//        if (livingEntity instanceof PlayerEntity && livingEntity.getVehicle() instanceof FlyingWoodSwordEntity) {
//            if (this.model instanceof PlayerEntityModel) {
//                PlayerEntityModel<?> playerModel = (PlayerEntityModel<?>) this.model;
//
//                // 存储原始值
//                float rightLegPitch = playerModel.rightLeg.pitch;
//                float rightLegYaw = playerModel.rightLeg.yaw;
//                float leftLegPitch = playerModel.leftLeg.pitch;
//                float leftLegYaw = playerModel.leftLeg.yaw;
//
//                try {
//                    // 设置新的姿势
//                    playerModel.rightLeg.pitch = -0.5F;
//                    playerModel.rightLeg.yaw = 0.2F;
//                    playerModel.leftLeg.pitch = -0.5F;
//                    playerModel.leftLeg.yaw = -0.2F;
//
//                    // 取消骑乘姿势
//                    livingEntity.setPose(EntityPose.STANDING);
//
//                    // 调整位置
//                    matrixStack.translate(0.0F, 0.5F, 0.0F);
//                } finally {
//                    // 在渲染后恢复原始值
//                    playerModel.rightLeg.pitch = rightLegPitch;
//                    playerModel.rightLeg.yaw = rightLegYaw;
//                    playerModel.leftLeg.pitch = leftLegPitch;
//                    playerModel.leftLeg.yaw = leftLegYaw;
//                }
//            }
//        }
    }
}
