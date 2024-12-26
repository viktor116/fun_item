package com.soybeani.event.entity;


import com.soybeani.entity.vehicle.FlyingWoodSwordEntity;
import com.soybeani.items.ItemsRegister;
import com.soybeani.items.item.LightningSpyglassItem;
import com.soybeani.items.item.NirvanaSpyglassItem;
import com.soybeani.network.packet.KeyRPacket;
import com.soybeani.network.packet.PlayerControlPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
            if(player != null){
                if (client.player.getVehicle() instanceof FlyingWoodSwordEntity) {
                    FlyingWoodSwordEntity sword = (FlyingWoodSwordEntity)client.player.getVehicle() ;
                    boolean leftKey = client.options.leftKey.isPressed();
                    boolean rightKey = client.options.rightKey.isPressed();
                    boolean forwardKey = client.options.forwardKey.isPressed();
                    boolean backKey = client.options.backKey.isPressed();
                    boolean jumpKey = client.options.jumpKey.isPressed();
                    boolean sneakKey = client.options.sneakKey.isPressed();
                    ClientPlayNetworking.send(new PlayerControlPayload(leftKey, rightKey, forwardKey, backKey, jumpKey, sneakKey));
                }
            }
        });
    }
}
