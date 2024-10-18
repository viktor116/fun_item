package com.soybeani;

import com.soybeani.config.InitValue;
import com.soybeani.event.EventRegister;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

import java.util.List;

public class Fun_Item implements ModInitializer {

	@Override
	public void onInitialize() {
		EventRegister.initialize(); //事件初始化
		ItemsRegister.initialize(); //注册物品
		InitValue.LOGGER.info("soybeani fun items start successful q(≧▽≦q)!");
	}
}