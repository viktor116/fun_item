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
        matrices.scale(2,2,2);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                new ItemStack(Items.WOODEN_SWORD),
                ModelTransformationMode.FIXED,
                light,
                1,
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
