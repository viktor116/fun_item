package com.soybeani.mixin;

import com.soybeani.config.InitValue;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.mixin.client.indigo.renderer.ItemRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.soybeani.items.ItemsRegister.LIGHTNING_SPYGLASS_IN_HAND;

/**
 * @author soybean
 * @date 2024/10/31 17:11
 * @description
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    private static final ModelIdentifier LIGHTLING_SPYGLASS_IN_HAND = new ModelIdentifier(Identifier.of(InitValue.MOD_ID, "lightning_spyglass_in_hand"), "inventory");
    @Shadow
    private @Final ItemModels models;

//    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
//    public BakedModel useSpyglassModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
//        if (stack.isOf(ItemsRegister.LIGHTNING_SPYGLASS) && renderMode != ModelTransformationMode.GUI) {
//            return models.getModelManager().getModel(LIGHTLING_SPYGLASS_IN_HAND);
//        }
//        return value;
//    }

    @Inject(method = "renderItem", at = @At("HEAD"))
    private void onRenderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (stack.isOf(ItemsRegister.LIGHTNING_SPYGLASS)) {
            // 第三人称渲染 - 使用时
            if (renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND ||
                    renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {

                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null && stack.getUseAction() == UseAction.SPYGLASS &&
                        client.player.getActiveItem() == stack) {  // 检查当前激活的物品是否为这个望远镜
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
    }

    @Inject(method = "renderItem", at = @At("RETURN"))
    private void afterRenderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (stack.isOf(ItemsRegister.LIGHTNING_SPYGLASS)) {
            if (renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND ||
                    renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {

                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null && stack.getUseAction() == UseAction.SPYGLASS &&
                        client.player.getActiveItem() == stack) {
                    matrices.pop();
                }
            }
        }
    }

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useSpyglassModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ItemsRegister.LIGHTNING_SPYGLASS) && renderMode != ModelTransformationMode.GUI) {
            return models.getModelManager().getModel(LIGHTLING_SPYGLASS_IN_HAND);
        }
        return value;
    }

}