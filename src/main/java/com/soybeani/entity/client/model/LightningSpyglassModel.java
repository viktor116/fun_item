package com.soybeani.entity.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.function.Function;

/**
 * @author soybean
 * @date 2024/10/28 15:20
 * @description
 */
public class LightningSpyglassModel extends Model {
    public LightningSpyglassModel(Function<Identifier, RenderLayer> layerFactory) {
        super(layerFactory);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {

    }
}
