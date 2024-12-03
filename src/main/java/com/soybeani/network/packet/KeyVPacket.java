package com.soybeani.network.packet;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.FlyBoatEntity;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import com.soybeani.items.ItemsRegister;
import com.soybeani.items.item.LightningSpyglassItem;
import com.soybeani.items.item.NirvanaSpyglassItem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * @author soybean
 * @date 2024/10/25 15:45
 * @description
 */
public class KeyVPacket implements CustomPayload {
    public static KeyVPacket INSTANCE = new KeyVPacket();

    public static final CustomPayload.Id<KeyVPacket> ID = new CustomPayload.Id<>(Identifier.of(InitValue.MOD_ID, "key_v"));
    public static final PacketCodec<PacketByteBuf, KeyVPacket> CODEC =  PacketCodec.unit(INSTANCE);
    public static void receive(KeyVPacket payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();
        if(player.hasVehicle()){
            if(player.getVehicle() instanceof Ice2BoatEntity boatEntity){ //冰霜王船
                boatEntity.setFreeze(!boatEntity.getFreeze());
                player.sendMessage(Text.of("冻结模式:"+ (boatEntity.getFreeze() ? "开启" : "关闭")),true);
                ServerPlayNetworking.send(player,INSTANCE);
            }
            if(player.getVehicle() instanceof FlyBoatEntity boatEntity){ //天空船
                boatEntity.setAccelerate(!boatEntity.getAccelerate());
                player.sendMessage(Text.of("加速模式:"+ (boatEntity.getAccelerate() ? "开启" : "关闭")),true);
                ServerPlayNetworking.send(player,INSTANCE);
            }
        }
        if(player.getMainHandStack().getItem() == ItemsRegister.LIGHTNING_SPYGLASS){ //大雷之镜
            LightningSpyglassItem spyglassItem =(LightningSpyglassItem) player.getMainHandStack().getItem();
            if(spyglassItem.getOpenLightning()){
                spyglassItem.setLevel(spyglassItem.getLevel()+1);
                if(spyglassItem.getLevel() > LightningSpyglassItem.LEVEL_MAX){
                    spyglassItem.setLevel(1);
                }
                player.sendMessage(Text.of("范围:"+ spyglassItem.getLevel()),true);
            }
        }
        if(player.getMainHandStack().getItem() == ItemsRegister.NIRVANA_SPYGLASS){ //寂灭之镜
            NirvanaSpyglassItem spyglassItem =(NirvanaSpyglassItem) player.getMainHandStack().getItem();
            if(spyglassItem.getOpenLightning()){
                spyglassItem.setSpyglassMode(spyglassItem.getSpyglassMode()+1);
                if(spyglassItem.getSpyglassMode() > NirvanaSpyglassItem.LEVEL_MAX){
                    spyglassItem.setSpyglassMode(1);
                }
                player.sendMessage(Text.of("当前模式:"+ NirvanaSpyglassItem.getModeName(spyglassItem.getSpyglassMode())),true);
            }
        }
    }

    public static void receiveOfClient(KeyVPacket payload, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.client().player;
        if(player.hasVehicle()){
            if(player.getVehicle() instanceof Ice2BoatEntity boatEntity){ //冰霜王船
                boatEntity.setFreeze(!boatEntity.getFreeze());
            }
            if(player.getVehicle() instanceof FlyBoatEntity boatEntity){ //天空船
                boatEntity.setAccelerate(!boatEntity.getAccelerate());
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
