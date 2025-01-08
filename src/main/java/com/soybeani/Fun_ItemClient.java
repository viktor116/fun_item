package com.soybeani;

import com.soybeani.block.ModBlock;
import com.soybeani.entity.EntityRegisterClient;
import com.soybeani.entity.client.ModEntityModelLayers;
import com.soybeani.event.EventRegister;
import com.soybeani.event.keybinds.KeyBindsInputHandler;
import com.soybeani.hud.HudRegister;
import com.soybeani.network.ModMessage;
import net.fabricmc.api.ClientModInitializer;

public class Fun_ItemClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModEntityModelLayers.registerModelLayers();
		ModMessage.registerS2CPackets();  // 注册服务端接收器
		KeyBindsInputHandler.register(); //register keyBinds
		ModBlock.initializeClient(); //client init
		EntityRegisterClient.initializeClient();
		HudRegister.Initialize();
		EventRegister.initializeClient();
	}
}