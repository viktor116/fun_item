package com.soybeani.network;

import com.soybeani.network.packet.KeyRPacket;
import com.soybeani.network.packet.KeyVPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * @author soybean
 * @date 2024/11/16 13:43
 * @description
 */

public class ModMessage {


    public static void registerC2SPackets(){
        PayloadTypeRegistry.playC2S().register(KeyRPacket.ID, KeyRPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(KeyVPacket.ID, KeyVPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(KeyRPacket.ID, KeyRPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(KeyVPacket.ID, KeyVPacket::receive);
    }

    public static void registerS2CPackets(){
        PayloadTypeRegistry.playS2C().register(KeyRPacket.ID, KeyRPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(KeyVPacket.ID, KeyVPacket.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(KeyRPacket.ID, KeyRPacket::receiveOfClient);
        ClientPlayNetworking.registerGlobalReceiver(KeyVPacket.ID, KeyVPacket::receiveOfClient);
    }
}
