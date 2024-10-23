package com.soybeani.network;

import com.soybeani.config.InitValue;
import com.soybeani.network.packet.ExampleC2SPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.registry.sync.SyncCompletePayload;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/22 16:16
 * @description
 */
public class ModMessage {
    public static final Identifier TOGGLE_ID = Identifier.of(InitValue.MOD_ID, "toggle");
    public static final Identifier TOGGLE_SYNC_ID = Identifier.of(InitValue.MOD_ID, "toggle_sync");

    public static void registerC2SPackets(){
        PayloadTypeRegistry.playC2S().register(ExampleC2SPacket.ID, ExampleC2SPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ExampleC2SPacket.ID, ExampleC2SPacket::receive);
    }

    public static void registerS2CPackets(){
    }
}
