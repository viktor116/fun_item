package com.soybeani.utils;

import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author soybean
 * @date 2024/10/9 16:46
 * @description
 */
public class CommonUtils {

    public static final Random RANDOM = new Random();
    public static void spawnParticlesAndPlaySound(World world, Entity entity, ParticleEffect particleType, SoundEvent soundEvent, SoundCategory soundCategory, int particleCount, double particleSpeed, double particleHeight, float volume, float pitch) {
        if (!world.isClient) {
            ((ServerWorld)world).spawnParticles(particleType,
                    entity.getX(), entity.getY(), entity.getZ(),
                    particleCount,
                    particleHeight, particleHeight, particleHeight,
                    particleSpeed
            );
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    soundEvent,
                    soundCategory,
                    volume, pitch);
        }
    }

    public static Random getRandom() {
        return RANDOM;
    }
}
