package com.soybeani.entity.client.renderer;

import com.soybeani.config.InitValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/12/9 15:56
 * @description
 */
@Environment(EnvType.CLIENT)
public class ZombieCreeperEntityRenderer extends CreeperEntityRenderer {
    private static final Identifier TEXTURE = InitValue.id("textures/entity/mob/zombie_creeper.png");

    public ZombieCreeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(CreeperEntity creeperEntity) {
        return TEXTURE;
    }
}
