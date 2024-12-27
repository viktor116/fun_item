package com.soybeani.event.entity;

import com.soybeani.entity.vehicle.FlyingWoodSwordEntity;
import com.soybeani.network.packet.PlayerControlPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author soybean
 * @date 2024/12/27 15:25
 * @description
 */
@Environment(EnvType.CLIENT)
public class EventTickClient {

    public static void registerClient(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;
            if(player != null){
                if (client.player.getVehicle() instanceof FlyingWoodSwordEntity) {
                    FlyingWoodSwordEntity sword = (FlyingWoodSwordEntity)client.player.getVehicle() ;
                    boolean forwardKey = client.options.forwardKey.isPressed();
                    boolean backKey = client.options.backKey.isPressed();
                    boolean leftKey = client.options.leftKey.isPressed();
                    boolean rightKey = client.options.rightKey.isPressed();
                    boolean jumpKey = client.options.jumpKey.isPressed();
                    boolean sneakKey = client.options.sneakKey.isPressed();
                    ClientPlayNetworking.send(new PlayerControlPayload(forwardKey, backKey, leftKey, rightKey, jumpKey, sneakKey));
                }
            }
        });
    }
}
