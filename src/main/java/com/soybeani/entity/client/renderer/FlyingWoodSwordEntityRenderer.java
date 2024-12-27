package com.soybeani.entity.client.renderer;

import com.soybeani.entity.custom.DiamondOreEntity;
import com.soybeani.entity.custom.HayBlockEntity;
import com.soybeani.entity.vehicle.FlyingWoodSwordEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

/**
 * @author soybean
 * @date 2024/12/26 14:49
 * @description
 */
public class FlyingWoodSwordEntityRenderer extends EntityRenderer<FlyingWoodSwordEntity> {

    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/item/wooden_sword.png");
    public FlyingWoodSwordEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 0.3F;
    }

    @Override
    public void render(FlyingWoodSwordEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        matrices.push();

        // 1. 首先移动到实体的位置中心
        matrices.translate(0.0, 0.4, 0.0);

        // 2. 应用实体的yaw旋转（使用平滑的yaw值）
        float interpolatedYaw = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-interpolatedYaw));

        // 3. 缩放
        matrices.scale(2, 2, 2);

        // 4. 应用基础旋转使剑处于正确的朝向
        // 先绕Z轴旋转-90度使剑水平
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-45));
        // 再绕Y轴旋转90度调整剑的朝向
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        // 渲染物品
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                new ItemStack(Items.WOODEN_SWORD),
                ModelTransformationMode.FIXED,
                light,
                OverlayTexture.DEFAULT_UV,  // 添加遗漏的OverlayTexture参数
                matrices,
                vertexConsumers,
                entity.getWorld(),
                0
        );

        matrices.pop();
    }

    @Override
    public Identifier getTexture(FlyingWoodSwordEntity entity) {
        return TEXTURE;
    }
}
