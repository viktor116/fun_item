package com.soybeani.entity.client.renderer;

import com.soybeani.config.InitValue;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

/**
 * @author soybean
 * @date 2024/10/28 17:55
 * @description
 */
public class LightningSpyglassRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    private final MinecraftClient client;
    private static BakedModel model;
    private static final Identifier MODEL_ID = Identifier.of(InitValue.MOD_ID, "lightning_spyglass_in_hand");
    private ItemRenderer itemRenderer;
    private static final ModelIdentifier SPYGLASS = ModelIdentifier.ofInventoryVariant(Identifier.ofVanilla("spyglass"));

    public LightningSpyglassRenderer() {
        this.client = MinecraftClient.getInstance();
        this.itemRenderer = client.getItemRenderer();
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        // 如果模型未加载，获取模型
        if (model == null) {
            model = client.getBakedModelManager().getModel(MODEL_ID);
        }

        // 根据不同的渲染模式应用不同的变换
        switch (mode) {
            case FIRST_PERSON_RIGHT_HAND -> {
                // 第一人称右手
                matrices.translate(0.5, 0.5, 0.5);
                matrices.scale(1.0f, 1.0f, 1.0f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-20.0F));
                matrices.translate(0.0, 0.2, 0.0);
            }
            case FIRST_PERSON_LEFT_HAND -> {
                // 第一人称左手
                matrices.translate(0.5, 0.5, 0.5);
                matrices.scale(1.0f, 1.0f, 1.0f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-45.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-20.0F));
                matrices.translate(0.0, 0.2, 0.0);
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                // 第三人称
                matrices.translate(0.5, 0.5, 0.5);
                matrices.scale(0.85f, 0.85f, 0.85f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-45.0F));
                matrices.translate(0.0, 0.2, 0.0);
            }
            case GUI -> {
                // 物品栏中的渲染
                matrices.translate(0.5, 0.5, 0.5);
                matrices.scale(0.8f, 0.8f, 0.8f);
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45.0F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
            }
            case GROUND -> {
                // 掉落在地上的渲染
                matrices.translate(0.5, 0.25, 0.5);
                matrices.scale(0.7f, 0.7f, 0.7f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
            }
            case FIXED -> {
                // 展示框中的渲染
                matrices.translate(0.5, 0.5, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            }
            default -> {
                // 其他情况
                matrices.translate(0.5, 0.5, 0.5);
            }
        }

        // 渲染模型
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE));
        BakedModelManager bakedModelManager = client.getBakedModelManager();

        itemRenderer.renderItem(stack, mode,false,matrices,vertexConsumers,light,overlay,bakedModelManager.getModel(SPYGLASS));

        matrices.pop();
    }
}
