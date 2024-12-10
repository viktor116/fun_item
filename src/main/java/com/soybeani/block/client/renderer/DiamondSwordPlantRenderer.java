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

            matrices.translate(0.5, 0.0, 0.5);
            
            // 随着生长升高（从0.0开始，最高升到0.5）
            matrices.translate(0.0, block.getScale(state) * 0.5, 0.0);

//            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-135));
            matrices.scale(scale*2, scale*2, scale*2);
            
            // 调整回中心点
            matrices.translate(-0.5, 0.0, -0.5);

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