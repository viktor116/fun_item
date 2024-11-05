package com.soybeani.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.soybeani.items.ItemsRegister;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;

/**
 * @author soybean
 * @date 2024/11/5 13:53
 * @description
 */
public class SpyglassHudOverlay {
    private static final Identifier SPYGLASS_SCOPE = new Identifier("textures/misc/spyglass_scope.png");
    private static final Identifier SPYGLASS_SCOPE_LIGHT = new Identifier("textures/misc/spyglass_scope_light.png");
    private final MinecraftClient client;

    public SpyglassHudOverlay(MinecraftClient client) {
        this.client = client;
    }

    public void render(MatrixStack matrices, float tickDelta) {
        ClientPlayerEntity player = client.player;
        if (player == null || !player.isUsingSpyglass() || !player.getActiveItem().isOf(ItemsRegister.LIGHTNING_SPYGLASS)) {
            return;
        }

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        // 设置混合模式
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // 获取屏幕尺寸
        Window window = client.getWindow();
        int width = window.getScaledWidth();
        int height = window.getScaledHeight();
        float scopeScale = client.options.getFovEffectScale().getValue().floatValue();

        // 计算范围大小
        float diameter = 2.0F * scopeScale;
        float radiusWidth = Math.min((float)width / 2.0F, (float)height / diameter);
        float boxSize = (float)height * diameter;
        float boxX = ((float)width - boxSize) / 2.0F;
        float boxY = ((float)height - boxSize) / 2.0F;

        // 渲染黑色背景
        matrices.push();
        matrices.translate(boxX, boxY, 0.0F);
        matrices.scale(boxSize, boxSize, 1.0F);

        RenderSystem.setShaderTexture(0, SPYGLASS_SCOPE);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0D, 1.0D, -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(1.0D, 1.0D, -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(1.0D, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        matrices.pop();

        // 渲染范围指示器
        RenderSystem.setShaderTexture(0, SPYGLASS_SCOPE_LIGHT);
        matrices.push();
        matrices.translate((float)width / 2.0F, (float)height / 2.0F, 0.0F);
        matrices.scale(radiusWidth, radiusWidth, 1.0F);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(-1.0D, 1.0D, -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(1.0D, 1.0D, -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(1.0D, -1.0D, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(-1.0D, -1.0D, -90.0D).texture(0.0F, 0.0F).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        matrices.pop();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}