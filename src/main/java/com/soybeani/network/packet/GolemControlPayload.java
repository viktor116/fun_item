package com.soybeani.network.packet;

import com.soybeani.config.InitValue;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.util.UUID;

/**
 * @author soybean
 * @date 2024/12/24 17:29
 * @description
 */
public record GolemControlPayload(boolean forward, boolean back, boolean left, boolean right, boolean jump) implements CustomPayload {
    public static final Id<GolemControlPayload> ID = new Id<>(InitValue.id("golem_control"));

    public static final PacketCodec<RegistryByteBuf, GolemControlPayload> PACKET_CODEC = new PacketCodec<>(){
        @Override
        public void encode(RegistryByteBuf buf, GolemControlPayload value) {
            buf.writeBoolean(value.forward());
            buf.writeBoolean(value.back());
            buf.writeBoolean(value.left());
            buf.writeBoolean(value.right());
            buf.writeBoolean(value.jump());
        }

        @Override
        public GolemControlPayload decode(RegistryByteBuf buf) {
            boolean forward = buf.readBoolean();
            boolean back = buf.readBoolean();
            boolean left = buf.readBoolean();
            boolean right = buf.readBoolean();
            boolean jump = buf.readBoolean();
            return new GolemControlPayload(forward, back, left, right, jump);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
