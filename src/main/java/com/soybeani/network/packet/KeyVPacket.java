package com.soybeani.network.packet;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/25 15:45
 * @description
 */
public class KeyVPacket implements CustomPayload {
    public static KeyVPacket INSTANCE = new KeyVPacket();

    public static final CustomPayload.Id<KeyVPacket> ID = new CustomPayload.Id<>(Identifier.of(InitValue.MOD_ID, "key_t"));
    public static final PacketCodec<PacketByteBuf, KeyVPacket> CODEC = PacketCodec.unit(INSTANCE);
    public static void receive(KeyVPacket payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();
        if(player.hasVehicle()){
            if(player.getVehicle() instanceof Ice2BoatEntity boatEntity){
                boatEntity.setFreeze(!boatEntity.getFreeze());
                player.sendMessage(Text.of("冻结模式"+ (boatEntity.getFreeze() ? "开启" : "关闭")),true);
                ServerPlayNetworking.send(player,INSTANCE);
            }
        }
    }

    public static void receiveOfClient(KeyVPacket payload, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.client().player;
        if(player.hasVehicle()){
            if(player.getVehicle() instanceof Ice2BoatEntity boatEntity){
                boatEntity.setFreeze(!boatEntity.getFreeze());
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}