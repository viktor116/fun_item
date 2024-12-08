package com.soybeani.entity.client.renderer;

import com.soybeani.entity.custom.DiamondOreEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class DiamondOreEntityRenderer extends EntityRenderer<DiamondOreEntity> {
    private final BlockRenderManager blockRenderManager;

    public DiamondOreEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.blockRenderManager = ctx.getBlockRenderManager();
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(DiamondOreEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
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
        this.blockRenderManager.renderBlockAsEntity(Blocks.DIAMOND_ORE.getDefaultState(), matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(DiamondOreEntity entity) {
        return Identifier.ofVanilla("textures/block/diamond_ore.png");
    }
} 