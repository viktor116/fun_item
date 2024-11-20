package com.soybeani.event.entity;

import com.soybeani.items.effect.LapisStatusEffect;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

/**
 * @author soybean
 * @date 2024/10/9 15:39
 * @description 使用物品相关事件(右键)
 */
public class EventBreak {
    public static void register(){
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            LapisStatusEffect.EventRegister(player, blockEntity, world, state, pos);
        });
    }
}
