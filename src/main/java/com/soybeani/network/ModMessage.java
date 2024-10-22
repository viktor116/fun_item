package com.soybeani.network;

import com.soybeani.config.InitValue;
import com.soybeani.network.packet.ExampleC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.registry.sync.SyncCompletePayload;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/22 16:16
 * @description
 */
public class ModMessage {
    public static final Identifier TOGGLE_ID = Identifier.of(InitValue.MOD_ID, "toggle");
    public static final Identifier TOGGLE_SYNC_ID = Identifier.of(InitValue.MOD_ID, "toggle_sync");
    public static final Identifier EXAMPLE_ID = Identifier.of(InitValue.MOD_ID, "example");

    public static void registerC2SPackets(){
//        ServerPlayNetworking.registerGlobalReceiver(getId(EXAMPLE_ID), ExampleC2SPacket::receive);
    }

    public static void registerS2CPackets(){

    }

    public static CustomPayload.Id getId(Identifier identifier){
        return new CustomPayload.Id(identifier);
    }
}
