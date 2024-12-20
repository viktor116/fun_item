package com.soybeani.entity.client.renderer;

import com.soybeani.entity.custom.PurpleLightningEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;

/**
 * @author soybean
 * @date 2024/11/6 15:09
 * @description
 */
public class PurpleLightningRenderer extends LightningEntityRenderer {
    public PurpleLightningRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(LightningEntity lightningEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float[] fs = new float[8];
        float[] gs = new float[8];
        float h = 0.0f;
        float j = 0.0f;
        Random random = Random.create(lightningEntity.seed);

        // 初始化闪电的分支位置
        for (int k = 7; k >= 0; --k) {
            fs[k] = h;
            gs[k] = j;
            h += (float)(random.nextInt(11) - 5);
            j += (float)(random.nextInt(11) - 5);
        }

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        // 渲染闪电的多个层次
        for (int l = 0; l < 4; ++l) {
            Random random2 = Random.create(lightningEntity.seed);
            for (int m = 0; m < 3; ++m) {
                int n = 7;
                int o = 0;
                if (m > 0) {
                    n = 7 - m;
                }
                if (m > 0) {
                    o = n - 2;
                }
                float p = fs[n] - h;
                float q = gs[n] - j;

                for (int r = n; r >= o; --r) {
                    float s = p;
                    float t = q;
                    if (m == 0) {
                        p += (float)(random2.nextInt(11) - 5);
                        q += (float)(random2.nextInt(11) - 5);
                    } else {
                        p += (float)(random2.nextInt(31) - 15);
                        q += (float)(random2.nextInt(31) - 15);
                    }

                    // 修改这里的RGB值来改变闪电颜色
                    float red = 0.431f;    // 110/255
                    float green = 0.020f;  // 5/255
                    float blue = 0.686f;   // 175/255

                    float y = 0.1f + (float)l * 0.2f;
                    if (m == 0) {
                        y *= (float)r * 0.1f + 1.0f;
                    }
                    float z = 0.1f + (float)l * 0.2f;
                    if (m == 0) {
                        z *= ((float)r - 1.0f) * 0.1f + 1.0f;
                    }

                    // 绘制闪电的四个分支
                    drawBranch(matrix4f, vertexConsumer, p, q, r, s, t, red, green, blue, y, z, false, false, true, false);
                    drawBranch(matrix4f, vertexConsumer, p, q, r, s, t, red, green, blue, y, z, true, false, true, true);
                    drawBranch(matrix4f, vertexConsumer, p, q, r, s, t, red, green, blue, y, z, true, true, false, true);
                    drawBranch(matrix4f, vertexConsumer, p, q, r, s, t, red, green, blue, y, z, false, true, false, false);
                }
            }
        }
    }

    private static void drawBranch(Matrix4f matrix, VertexConsumer buffer, float x1, float z1, int y, float x2, float z2, float red, float green, float blue, float offset2, float offset1, boolean shiftEast1, boolean shiftSouth1, boolean shiftEast2, boolean shiftSouth2) {
        buffer.vertex(matrix, x1 + (shiftEast1 ? offset1 : -offset1), (float)(y * 16), z1 + (shiftSouth1 ? offset1 : -offset1)).color(red, green, blue, 0.3f);
        buffer.vertex(matrix, x2 + (shiftEast1 ? offset2 : -offset2), (float)((y + 1) * 16), z2 + (shiftSouth1 ? offset2 : -offset2)).color(red, green, blue, 0.3f);
        buffer.vertex(matrix, x2 + (shiftEast2 ? offset2 : -offset2), (float)((y + 1) * 16), z2 + (shiftSouth2 ? offset2 : -offset2)).color(red, green, blue, 0.3f);
        buffer.vertex(matrix, x1 + (shiftEast2 ? offset1 : -offset1), (float)(y * 16), z1 + (shiftSouth2 ? offset1 : -offset1)).color(red, green, blue, 0.3f);
    }

    @Override
    public Identifier getTexture(LightningEntity lightningEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
