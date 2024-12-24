package com.soybeani.network.packet;

import com.soybeani.config.InitValue;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * @author soybean
 * @date 2024/12/24 17:31
 * @description
 */
public record GolemRotationPayload(float yaw, float pitch) implements CustomPayload {
    public static final Id<GolemRotationPayload> ID = new Id<>(InitValue.id("golem_rotation"));

    public static final PacketCodec<RegistryByteBuf, GolemRotationPayload> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public GolemRotationPayload decode(RegistryByteBuf buf) {
            float yaw = buf.readFloat();
            float pitch = buf.readFloat();
            return new GolemRotationPayload(yaw, pitch);
        }

        @Override
        public void encode(RegistryByteBuf buf, GolemRotationPayload value) {
            buf.writeFloat(value.yaw());
            buf.writeFloat(value.yaw());
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
