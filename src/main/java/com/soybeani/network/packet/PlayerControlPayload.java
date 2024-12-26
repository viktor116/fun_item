package com.soybeani.network.packet;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.FlyBoatEntity;
import com.soybeani.entity.vehicle.FlyingWoodSwordEntity;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * @author soybean
 * @date 2024/12/26 17:28
 * @description
 */
public record PlayerControlPayload (boolean forward, boolean back, boolean left, boolean right, boolean jump,boolean sneak) implements CustomPayload {
    public static final Id<PlayerControlPayload> ID = new Id<>(InitValue.id("player_control"));

    public static final PacketCodec<RegistryByteBuf, PlayerControlPayload> PACKET_CODEC = new PacketCodec<>(){
        @Override
        public void encode(RegistryByteBuf buf, PlayerControlPayload value) {
            buf.writeBoolean(value.forward());
            buf.writeBoolean(value.back());
            buf.writeBoolean(value.left());
            buf.writeBoolean(value.right());
            buf.writeBoolean(value.jump());
            buf.writeBoolean(value.sneak());
        }

        @Override
        public PlayerControlPayload decode(RegistryByteBuf buf) {
            boolean forward = buf.readBoolean();
            boolean back = buf.readBoolean();
            boolean left = buf.readBoolean();
            boolean right = buf.readBoolean();
            boolean jump = buf.readBoolean();
            boolean sneak = buf.readBoolean();
            return new PlayerControlPayload(forward, back, left, right, jump, sneak);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(PlayerControlPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        if (player.hasVehicle()) {
            if (player.getVehicle() instanceof FlyingWoodSwordEntity woodSwordEntity) { //飞行木剑
                woodSwordEntity.setInputs(payload.forward(), payload.back(), payload.left(), payload.right(),payload.jump(),payload.sneak());
            }
        }
    }

    public static void receiveClient(PlayerControlPayload payload, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.player();
        if (player.hasVehicle()) {
            if (player.getVehicle() instanceof FlyingWoodSwordEntity woodSwordEntity) { //飞行木剑
                woodSwordEntity.setInputs(payload.forward(), payload.back(), payload.left(), payload.right(),payload.jump(),payload.sneak());
            }
        }
    }
}
