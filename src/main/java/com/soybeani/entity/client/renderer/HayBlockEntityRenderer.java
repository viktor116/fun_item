package com.soybeani.entity.client.renderer;

import com.soybeani.entity.custom.HayBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HayBlockEntityRenderer extends EntityRenderer<HayBlockEntity> {
    private final BlockRenderManager blockRenderManager;

    public HayBlockEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.blockRenderManager = ctx.getBlockRenderManager();
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(HayBlockEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        
        if(entity.isBaby()) {
            matrices.scale(.6f, .6f, .6f);
        }
        int overlay = OverlayTexture.DEFAULT_UV;
        if (entity.getHurtTime() > 0) {
            overlay = OverlayTexture.packUv(OverlayTexture.getU(0.0F), OverlayTexture.getV(true));
        }
        matrices.translate(-0.5D, 0.0D, -0.5D);
        this.blockRenderManager.renderBlockAsEntity(Blocks.HAY_BLOCK.getDefaultState(), matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(HayBlockEntity entity) {
        // 因为我们直接渲染方块，这个方法不会被使用，但仍需要实现
        return Identifier.ofVanilla("textures/block/hay_block_side.png");
    }
} 