package com.soybeani.network.handler;

import com.soybeani.items.item.TalismanItem;
import com.soybeani.network.packet.GolemControlPayload;
import com.soybeani.network.packet.GolemRotationPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.passive.IronGolemEntity;

/**
 * @author soybean
 * @date 2024/12/24 17:37
 * @description
 */
public class NetworkHandler {
//    public static void registerC2SPackets() {
//        ServerPlayNetworking.registerGlobalReceiver(GolemControlPayload.ID, (payload, context) -> {
//            // 在服务器端处理移动控制包
//            IronGolemEntity golem = TalismanItem.getControlledGolem(player.player().getUuid());
//            if (golem != null && golem.isAlive()) {
//                // 计算移动向量
//                double yaw = Math.toRadians(player.player().getYaw());
//                double moveX = 0;
//                double moveZ = 0;
//
//                if (packet.forward()) {
//                    moveX -= Math.sin(yaw);
//                    moveZ += Math.cos(yaw);
//                }
//                if (packet.back()) {
//                    moveX += Math.sin(yaw);
//                    moveZ -= Math.cos(yaw);
//                }
//                if (packet.left()) {
//                    moveX -= Math.cos(yaw);
//                    moveZ -= Math.sin(yaw);
//                }
//                if (packet.right()) {
//                    moveX += Math.cos(yaw);
//                    moveZ += Math.sin(yaw);
//                }
//
//                // 应用移动
//                double length = Math.sqrt(moveX * moveX + moveZ * moveZ);
//                if (length > 0) {
//                    moveX /= length;
//                    moveZ /= length;
//                    golem.setVelocity(moveX * 0.4, packet.jump() ? 0.5 : golem.getVelocity().y, moveZ * 0.4);
//                } else if (!packet.jump()) {
//                    golem.setVelocity(0, golem.getVelocity().y, 0);
//                }
//            }
//        });
//
//        ServerPlayNetworking.registerGlobalReceiver(GolemRotationPayload.ID, (payload, context) -> {
//            // 在服务器端处理旋转包
//            IronGolemEntity golem = TalismanItem.getControlledGolem(player.player().getUuid());
//            if (golem != null && golem.isAlive()) {
//                golem.setYaw(packet.yaw());
//                golem.setPitch(packet.pitch());
//                golem.setHeadYaw(packet.yaw());
//                golem.prevYaw = packet.yaw();
//                golem.prevPitch = packet.pitch();
//            }
//        });
//    }

    public static void sendControlPacket(boolean forward, boolean back, boolean left, boolean right, boolean jump) {
        ClientPlayNetworking.send(new GolemControlPayload(forward, back, left, right, jump));
    }

    public static void sendRotationPacket(float yaw, float pitch) {
        ClientPlayNetworking.send(new GolemRotationPayload(yaw, pitch));
    }
}
