package com.soybeani.items.item;

import com.soybeani.config.InitValue;
import com.soybeani.utils.RayCastUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class PregnantSpyglassItem extends SpyglassItem {
    public PregnantSpyglassItem(Settings settings) {
        super(settings);
    }

    public void lookPregnant(PlayerEntity user,ItemStack itemStack) {
        World world = user.getWorld();
        if (world.isClient) return;
        Entity targetEntity = RayCastUtils.getTargetEntity(world, user, 200.0);
        if (targetEntity instanceof AnimalEntity animalEntity) {
            if (!animalEntity.isBaby()) {
                InitValue.LOGGER.info("animalEntity exist");
                animalEntity.setBreedingAge(0); // 确保繁殖冷却已结束
                animalEntity.setLoveTicks(600);
                itemStack.setDamage(itemStack.getDamage() + 1);
                spawnLoveParticles(world, animalEntity);
                // 播放音效
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    private void spawnLoveParticles(World world, Entity entity) {
        for (int i = 0; i < 7; i++) {
            double d = world.random.nextGaussian() * 0.02D;
            double e = world.random.nextGaussian() * 0.02D;
            double f = world.random.nextGaussian() * 0.02D;
            world.addParticle(
                    ParticleTypes.HEART,
                    entity.getParticleX(1.0D),
                    entity.getRandomBodyY() + 0.5D,
                    entity.getParticleZ(1.0D),
                    d, e, f
            );
        }
    }
}
