package com.soybeani.mixin;

import com.soybeani.config.InitValue;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.mixin.client.indigo.renderer.ItemRendererAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useSpyglassModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ItemsRegister.LIGHTNING_SPYGLASS) && renderMode != ModelTransformationMode.GUI) {
            return models.getModelManager().getModel(LIGHTLING_SPYGLASS_IN_HAND);
        }
        return value;
    }
}