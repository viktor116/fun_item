package com.soybeani;

import com.soybeani.block.ModBlock;
import com.soybeani.entity.EntityRegisterClient;
import com.soybeani.event.keybinds.KeyBindsInputHandler;
import com.soybeani.hud.HudRegister;
import com.soybeani.network.ModMessage;
import net.fabricmc.api.ClientModInitializer;

public class Fun_ItemClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBindsInputHandler.register(); //register keyBinds
		ModBlock.initializeClient(); //client init
		ModMessage.registerC2SPackets();  // 注册服务端接收器
		EntityRegisterClient.initializeClient();
		HudRegister.Initialize();
	}
}