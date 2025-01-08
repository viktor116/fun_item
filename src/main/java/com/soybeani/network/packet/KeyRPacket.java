package com.soybeani.network.packet;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.BoatAbility;
import com.soybeani.entity.vehicle.FlyBoatEntity;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import com.soybeani.items.ItemsRegister;
import com.soybeani.items.item.LightningSpyglassItem;
import com.soybeani.items.item.NirvanaSpyglassItem;
import com.soybeani.items.item.PregnantSpyglassItem;
import com.soybeani.items.item.TntBatonItem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/22 16:46
 * @description
 */
public record KeyRPacket() implements CustomPayload{

    public static final KeyRPacket INSTANCE = new KeyRPacket();
    public static final CustomPayload.Id<KeyRPacket> ID = new CustomPayload.Id<>(Identifier.of(InitValue.MOD_ID, "key_r"));
    public static final PacketCodec<PacketByteBuf, KeyRPacket> CODEC = PacketCodec.unit(INSTANCE);

    public static void receive(KeyRPacket payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();
        if(player.hasVehicle()){
            if(player.getVehicle() instanceof BoatAbility boatEntity){
                boatEntity.setFly(!boatEntity.getFly());
                player.sendMessage(Text.of("飞行模式:"+ (boatEntity.getFly() ? "开启" : "关闭")),true);
                ServerPlayNetworking.send(player,new KeyRPacket());
            }
            if(player.getVehicle() instanceof FlyBoatEntity flyBoatEntity){
                flyBoatEntity.SwitchFly();
                Integer fly = flyBoatEntity.getFly();
                String sendText;
                switch (fly){
                    case 0:
                        sendText = "关闭";
                        break;
                    case 1:
                        sendText = "开启";
                        break;
                    case 2:
                        sendText = "恒定";
                        break;
                    default:
                        sendText = "出错了";
                        break;
                }
                player.sendMessage(Text.of("飞行模式:" + sendText),true);
                ServerPlayNetworking.send(player,new KeyRPacket());
            }
        }
        //大雷之镜
        if(player.getMainHandStack().getItem() == ItemsRegister.LIGHTNING_SPYGLASS){
            LightningSpyglassItem spyglassItem =(LightningSpyglassItem) player.getMainHandStack().getItem();
            spyglassItem.setOpenLightning(!spyglassItem.getOpenLightning());
            player.sendMessage(Text.of("大雷模式:"+ (spyglassItem.getOpenLightning() ? "开启" : "关闭")),true);
        }
        //寂寞之镜
        if(player.getMainHandStack().getItem() == ItemsRegister.NIRVANA_SPYGLASS){
            NirvanaSpyglassItem spyglassItem =(NirvanaSpyglassItem) player.getMainHandStack().getItem();
            spyglassItem.setOpenLightning(!spyglassItem.getOpenLightning());
            player.sendMessage(Text.of("大雷模式:"+ (spyglassItem.getOpenLightning() ? "开启" : "关闭")),true);
        }
        //TNT指挥棒
        if(player.getMainHandStack().getItem() instanceof TntBatonItem tntBatonItem){
            tntBatonItem.switchMode();
            tntBatonItem.sendSwitchModeMessage(player);
        }
    }

    public static void receiveOfClient(KeyRPacket payload, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.client().player;
        if(player.hasVehicle()){
            if(player.getVehicle() instanceof BoatAbility boatEntity){
                boatEntity.setFly(!boatEntity.getFly());
            }
            if(player.getVehicle() instanceof FlyBoatEntity flyBoatEntity){
                flyBoatEntity.SwitchFly();
            }
            if(player.getMainHandStack().getItem() instanceof TntBatonItem tntBatonItem){
                tntBatonItem.switchMode();
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
