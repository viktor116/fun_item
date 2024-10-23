package com.soybeani;

import com.soybeani.entity.EntityRegister;
import com.soybeani.entity.client.renderer.Su7CarRenderer;
import com.soybeani.event.keybinds.KeyBindsInputHandler;
import com.soybeani.network.ModMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class Fun_ItemClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBindsInputHandler.register(); //register keyBinds
		ModMessage.registerC2SPackets(); //network
		EntityRendererRegistry.register(EntityRegister.SU7, Su7CarRenderer::new);
	}
}