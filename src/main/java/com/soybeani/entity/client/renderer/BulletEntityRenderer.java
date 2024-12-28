package com.soybeani.entity.client.renderer;

import com.soybeani.config.InitValue;
import com.soybeani.entity.custom.BulletEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Map;

/**
 * @author soybean
 * @date 2024/12/28 14:41
 * @description
 */
public class BulletEntityRenderer extends EntityRenderer<BulletEntity> {
    // 子弹的大小
    private static final float SIZE = 0.15F;

    // 子弹的颜色映射
    private static final Map<BulletEntity.Type, Color> BULLET_COLORS = Map.of(
            BulletEntity.Type.COPPER, new Color(0xB87333), // 铜的颜色
            BulletEntity.Type.IRON, new Color(0x808080)    // 铁的颜色
    );

    public BulletEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(BulletEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // 设置渲染位置
        matrices.translate(0, SIZE/2, 0);

        // 根据实体的速度设置旋转
        Vec3d velocity = entity.getVelocity();
        float pitch = (float) Math.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(pitch));

        // 获取顶点消费者
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(this.getTexture(entity)));

        // 获取子弹颜色
        Color color = BULLET_COLORS.get(entity.hasType() ? entity.getBulletType() : BulletEntity.Type.COPPER);
        // 渲染方块
        matrices.scale(SIZE, SIZE, SIZE);
        renderBullet(matrices, vertexConsumer, light, color);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private void renderBullet(MatrixStack matrices, VertexConsumer vertices, int light, Color color) {
        // 基础颜色
        float baseRed = color.getRed() / 255.0F;
        float baseGreen = color.getGreen() / 255.0F;
        float baseBlue = color.getBlue() / 255.0F;

        // 阴影颜色 (稍微暗一些)
        float shadowRed = Math.max(baseRed * 0.7F, 0.0F);
        float shadowGreen = Math.max(baseGreen * 0.7F, 0.0F);
        float shadowBlue = Math.max(baseBlue * 0.7F, 0.0F);

        // 高光颜色 (稍微亮一些)
        float highlightRed = Math.min(baseRed * 1.3F, 1.0F);
        float highlightGreen = Math.min(baseGreen * 1.3F, 1.0F);
        float highlightBlue = Math.min(baseBlue * 1.3F, 1.0F);

        // 渲染正面
        renderFace(vertices, matrices, light, shadowRed, shadowGreen, shadowBlue, baseRed, baseGreen, baseBlue, highlightRed, highlightGreen, highlightBlue,
                -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f);

        // 渲染后面
        renderFace(vertices, matrices, light, shadowRed, shadowGreen, shadowBlue, baseRed, baseGreen, baseBlue, highlightRed, highlightGreen, highlightBlue,
                0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f);

        // 渲染上面
        renderFace(vertices, matrices, light, shadowRed, shadowGreen, shadowBlue, baseRed, baseGreen, baseBlue, highlightRed, highlightGreen, highlightBlue,
                -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f);

        // 渲染下面
        renderFace(vertices, matrices, light, shadowRed, shadowGreen, shadowBlue, baseRed, baseGreen, baseBlue, highlightRed, highlightGreen, highlightBlue,
                -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f);

        // 渲染右面
        renderFace(vertices, matrices, light, shadowRed, shadowGreen, shadowBlue, baseRed, baseGreen, baseBlue, highlightRed, highlightGreen, highlightBlue,
                0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f);

        // 渲染左面
        renderFace(vertices, matrices, light, shadowRed, shadowGreen, shadowBlue, baseRed, baseGreen, baseBlue, highlightRed, highlightGreen, highlightBlue,
                -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f);
    }

    private void renderFace(VertexConsumer vertices, MatrixStack matrices, int light,
                            float shadowRed, float shadowGreen, float shadowBlue,
                            float baseRed, float baseGreen, float baseBlue,
                            float highlightRed, float highlightGreen, float highlightBlue,
                            float x1, float y1, float z1, float x2, float y2, float z2,
                            float x3, float y3, float z3, float x4, float y4, float z4) {
        // 顶点顺序：左下 -> 右下 -> 右上 -> 左上
        vertex(vertices, matrices, x1, y1, z1, 0, 0, shadowRed, shadowGreen, shadowBlue, light);
        vertex(vertices, matrices, x2, y2, z2, 0.5f, 0, baseRed, baseGreen, baseBlue, light);
        vertex(vertices, matrices, x3, y3, z3, 1, 1, highlightRed, highlightGreen, highlightBlue, light);
        vertex(vertices, matrices, x4, y4, z4, 0, 1, baseRed, baseGreen, baseBlue, light);
    }

    private void vertex(VertexConsumer vertices, MatrixStack matrices, float x, float y, float z,
                        float u, float v, float red, float green, float blue, int light) {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        Matrix3f matrix3f = matrices.peek().getNormalMatrix();
        MatrixStack.Entry entry = matrices.peek(); // 获取 Entry
        vertices.vertex(matrix4f, x, y, z)
                .color(red, green, blue, 1.0F)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public Identifier getTexture(BulletEntity entity) {
        // 返回一个白色纹理，因为我们使用顶点颜色
        return InitValue.id("textures/entity/white.png");
    }
}