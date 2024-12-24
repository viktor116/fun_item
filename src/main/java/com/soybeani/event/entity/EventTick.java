package com.soybeani.event.entity;


import com.soybeani.items.ItemsRegister;
import com.soybeani.items.item.LightningSpyglassItem;
import com.soybeani.items.item.NirvanaSpyglassItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
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
                //大雷之镜
                if(serverPlayer.isUsingSpyglass() && serverPlayer.getMainHandStack().getItem() == ItemsRegister.LIGHTNING_SPYGLASS){
                    LightningSpyglassItem spyglassItem =(LightningSpyglassItem) serverPlayer.getMainHandStack().getItem();
                    spyglassItem.lookLightning((PlayerEntity) serverPlayer,spyglassItem.getLevel());
                }
                //寂灭之镜
                if(serverPlayer.isUsingSpyglass() && serverPlayer.getMainHandStack().getItem() == ItemsRegister.NIRVANA_SPYGLASS){
                    NirvanaSpyglassItem spyglassItem =(NirvanaSpyglassItem) serverPlayer.getMainHandStack().getItem();
                    spyglassItem.lookLightning((PlayerEntity) serverPlayer,spyglassItem.getSpyglassMode());
                }
            }
        });
    }

    public static void registerClient(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;
            //铁傀儡控制
            if (player != null && player.isSpectator()) {
                // 检查玩家是否在控制铁傀儡
                boolean forward = client.options.forwardKey.isPressed();
                boolean back = client.options.backKey.isPressed();
                boolean left = client.options.leftKey.isPressed();
                boolean right = client.options.rightKey.isPressed();
                boolean jump = client.options.jumpKey.isPressed();

                // 发送移动数据到服务器
                // 这里需要实现自定义的网络包发送
//                sendMovementPacket(forward, back, left, right, jump);
            }
        });
    }
}
