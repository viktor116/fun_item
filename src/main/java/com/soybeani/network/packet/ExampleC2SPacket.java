package com.soybeani.network.packet;

import com.soybeani.config.InitValue;
import com.soybeani.network.ModMessage;
import com.soybeani.utils.CommonUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.registry.sync.SyncCompletePayload;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.time.Instant;

/**
 * @author soybean
 * @date 2024/10/22 16:46
 * @description
 */
public record ExampleC2SPacket() implements CustomPayload{
    public static ExampleC2SPacket INSTANCE = new ExampleC2SPacket();

    public static final CustomPayload.Id<ExampleC2SPacket> ID = new CustomPayload.Id<>(Identifier.of(InitValue.MOD_ID, "example"));
    public static final PacketCodec<PacketByteBuf, ExampleC2SPacket> CODEC = PacketCodec.unit(INSTANCE);
    public static void receive(ExampleC2SPacket payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();
        EntityType.COW.spawn(server.getWorld(player.getWorld().getRegistryKey()),player.getBlockPos(),null);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
