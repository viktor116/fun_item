package com.soybeani.event.entity;


import com.soybeani.config.InitValue;
import com.soybeani.items.ItemsRegister;
import com.soybeani.items.item.LightningSpyglassItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

/**
 * @author soybean
 * @date 2024/10/24 10:20
 * @description
 */
public class EventTick {
    public static void register(){
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity serverPlayer : playerList) {
                //大雷之境
                if(serverPlayer.isUsingSpyglass() && serverPlayer.getMainHandStack().getItem() == ItemsRegister.LIGHTNING_SPYGLASS){
                    LightningSpyglassItem spyglassItem =(LightningSpyglassItem) serverPlayer.getMainHandStack().getItem();
                    spyglassItem.lookLightning((PlayerEntity) serverPlayer,spyglassItem.getLevel());
                }
            }
        });
    }
}
