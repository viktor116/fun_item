package com.soybeani;

import com.soybeani.block.ModBlock;
import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.event.EventRegister;
import com.soybeani.hud.HudRegister;
import com.soybeani.items.ItemsRegister;
import com.soybeani.network.ModMessage;
import com.soybeani.sound.SoundRegister;
import net.fabricmc.api.ModInitializer;

public class Fun_Item implements ModInitializer {

	@Override
	public void onInitialize() {
		EntityRegister.initialize(); //注册实体
		SoundRegister.initialize(); //声音注册
		ModMessage.registerC2SPackets();
		HudRegister.Initialize();
		ItemsRegister.initialize(); //注册物品
		ModBlock.initialize(); //方块初始化
		EventRegister.initialize(); //事件初始化

		InitValue.LOGGER.info("soybeani fun items start successful q(≧▽≦q)!");
	}
}