package com.soybeani.entity.client;

import com.soybeani.config.InitValue;
import com.soybeani.entity.client.model.ZombiePregnantEntityModel;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author soybean
 * @date 2025/1/7 10:52
 * @description
 */
public class ModEntityModelLayers {
    public static final EntityModelLayer ZOMBIE_PREGNANT = new EntityModelLayer(Identifier.of(InitValue.MOD_ID, "zombie_pregnant"), "main");
    // 注册模型层
    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(ZOMBIE_PREGNANT, ZombiePregnantEntityModel::getTexturedModelData);
    }
}
