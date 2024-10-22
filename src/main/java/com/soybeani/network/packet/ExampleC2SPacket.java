package com.soybeani.network.packet;

import com.soybeani.network.ModMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author soybean
 * @date 2024/10/22 16:46
 * @description
 */
public class ExampleC2SPacket implements CustomPayload{
    public static void receive(CustomPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();
        EntityType.COW.spawn(server.getWorld(player.getWorld().getRegistryKey()),player.getBlockPos(),null);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
    }
}
