package com.soybeani;

import com.soybeani.config.InitValue;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fun_Item implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(InitValue.MOD_ID);

	@Override
	public void onInitialize() {
		ItemsRegister.initialize(); //注册物品
		LOGGER.info("Hello Fabric world!");
	}
}