package com.soybeani.mixin;

import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.entity.custom.PurpleLightningEntity;
import com.soybeani.items.ItemsRegister;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author soybean
 * @date 2024/10/31 17:11
 * @description
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    private @Final ItemModels models;

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void onRenderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {

        // 第三人称渲染 - 使用时
        if (renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND ||
                renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && stack.getUseAction() == UseAction.SPYGLASS &&
                    client.player.getActiveItem() == stack) {  // 检查当前激活的物品是否为这个望远镜
                if (stack.isOf(ItemsRegister.LIGHTNING_SPYGLASS) || stack.isOf(ItemsRegister.NIRVANA_SPYGLASS)) {
                    matrices.push();
                    // 将物品移动到眼睛位置
                    if (renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) {
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(0f));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90f));
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90f));
                    } else {
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(0f));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90f));
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90f));
                    }
                }
            }
        }
        if (Registries.ITEM.getId(stack.getItem()).equals(Identifier.of(InitValue.MOD_ID, "purple_lightning"))) {
            ci.cancel();
            // 渲染生物模型
            PurpleLightningEntity lightningEntity = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, MinecraftClient.getInstance().world);
            matrices.push();
//            // 使用 org.joml.Quaternionf 进行旋转
//            Quaternionf rotation = new Quaternionf().rotateY((float) Math.toRadians(180));
//            matrices.multiply(rotation);

            matrices.scale(0.5F, 0.5F, 0.5F);// 调整缩放比例
            MinecraftClient.getInstance().getEntityRenderDispatcher().render(lightningEntity, 0, 0, 0, 0.0F, 1.0F, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }

    @Inject(method = "renderItem", at = @At("RETURN"))
    private void afterRenderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND ||
                renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && stack.getUseAction() == UseAction.SPYGLASS &&
                    client.player.getActiveItem() == stack) {
                if (stack.isOf(ItemsRegister.LIGHTNING_SPYGLASS) || stack.isOf(ItemsRegister.NIRVANA_SPYGLASS)) {
                    matrices.pop();
                }
            }
        }
    }

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useSpyglassModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (renderMode != ModelTransformationMode.GUI &&
                renderMode != ModelTransformationMode.GROUND &&
                renderMode != ModelTransformationMode.FIXED) {
            if (stack.isOf(ItemsRegister.LIGHTNING_SPYGLASS)) {
                return models.getModelManager().getModel(EntityRegister.LIGHTNING_SPYGLASS_IN_HAND);
            } else if (stack.isOf(ItemsRegister.NIRVANA_SPYGLASS)) {
                return models.getModelManager().getModel(EntityRegister.NIRVANA_SPYGLASS_IN_HAND);
            }
        }

        return value;
    }

}