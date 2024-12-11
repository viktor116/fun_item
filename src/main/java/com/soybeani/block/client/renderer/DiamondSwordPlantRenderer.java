package com.soybeani.block.client.renderer;

import com.soybeani.block.custom.DiamondSwordPlantBlock;
import com.soybeani.block.entity.DiamondSwordPlantEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.render.model.json.ModelTransformationMode;

public class DiamondSwordPlantRenderer implements BlockEntityRenderer<DiamondSwordPlantEntity> {

    public DiamondSwordPlantRenderer(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public void render(DiamondSwordPlantEntity entity, float tickDelta, MatrixStack matrices, 
                      VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        BlockState state = entity.getCachedState();

        if (state.getBlock() instanceof DiamondSwordPlantBlock block) {
            float scale = block.getScale(state);

            // 1. 移动到方块中心点
            matrices.translate(0.5, 0.0, 0.5);

            // 2. 调整基准高度，确保不会陷入地面
            matrices.translate(0.0, 0.2 + (scale * 0.3), 0.0);

            // 3. 先进行旋转，避免旋转影响位置
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(135));

            // 4. 应用缩放
            float adjustedScale = scale * 1.0f; // 减小缩放比例
            matrices.scale(adjustedScale, adjustedScale, adjustedScale);

            // 5. 最后进行微调，确保在方块正中心
            matrices.translate(0, 0.0, 0.0); // 略微向左调整

            // 渲染钻石剑物品模型
            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    new ItemStack(Items.DIAMOND_SWORD),
                    ModelTransformationMode.FIXED,
                    light,
                    overlay,
                    matrices,
                    vertexConsumers,
                    entity.getWorld(),
                    0
            );
        }
        matrices.pop();
    }
} 