package com.soybeani;

import com.soybeani.block.ModBlock;
import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.entity.client.renderer.Su7CarRenderer;
import com.soybeani.event.keybinds.KeyBindsInputHandler;
import com.soybeani.network.ModMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Fun_ItemClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBindsInputHandler.register(); //register keyBinds
		ModMessage.registerC2SPackets(); //network
		ModBlock.initializeClient(); //client init
		EntityRegister.initializeClient();
//		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
//				.registerReloadListener(new SimpleSynchronousResourceReloadListener() {
//					@Override
//					public Identifier getFabricId() {
//						return Identifier.of(InitValue.MOD_ID, "models");
//					}
//
//					@Override
//					public void reload(ResourceManager manager) {
//						// 在这里可以添加额外的模型加载逻辑
//					}
//				});
	}
}