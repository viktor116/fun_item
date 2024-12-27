package com.soybeani.event;

import com.soybeani.event.entity.*;
import com.soybeani.utils.DelayedTaskManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;

/**
 * @author soybean
 * @date 2024/10/9 15:18
 * @description 总事件
 */
public class EventRegister {
    public static void initialize() {
        EventTick.register();
        EventAttack.register();
        EventUse.register();
        EventBreak.register();
        DelayedTaskManager.init();
    }

    public static void initializeClient(){
        EventTickClient.registerClient();
    }
}
