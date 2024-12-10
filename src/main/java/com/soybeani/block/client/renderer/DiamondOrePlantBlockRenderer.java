package com.soybeani.block.client.renderer;

import com.soybeani.block.custom.CowPlantBlock;
import com.soybeani.block.custom.DiamondOrePlantBlock;
import com.soybeani.block.entity.DiamondOrePlantEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

/**
 * @author soybean
 * @date 2024/12/10 11:43
 * @description
 */
public class DiamondOrePlantBlockRenderer implements BlockEntityRenderer<DiamondOrePlantEntity> {
    private final BlockRenderManager blockRenderManager;

    public DiamondOrePlantBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(DiamondOrePlantEntity entity, float tickDelta, MatrixStack matrices, 
                      VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        BlockState state = entity.getCachedState();
        if (state.getBlock() instanceof DiamondOrePlantBlock block) {
            float scale = block.getScale(state);
            
            matrices.translate(0.5, 0.0, 0.5);
            matrices.scale(scale, scale, scale);
            matrices.translate(-0.5, 0.0, -0.5);


            this.blockRenderManager.renderBlockAsEntity(
                Blocks.DIAMOND_ORE.getDefaultState(),
                matrices,
                vertexConsumers,
                light,
                overlay
            );
        }
        matrices.pop();
    }
}
