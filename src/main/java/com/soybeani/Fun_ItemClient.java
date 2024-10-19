package com.soybeani;

import com.soybeani.entity.EntityRegister;
import com.soybeani.entity.client.renderer.Su7CarRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class Fun_ItemClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(EntityRegister.SU7, Su7CarRenderer::new);
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}