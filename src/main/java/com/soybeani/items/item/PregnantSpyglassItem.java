package com.soybeani.items.item;

import com.soybeani.utils.RayCastUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class PregnantSpyglassItem extends SpyglassItem {

    public PregnantSpyglassItem(Settings settings) {
        super(settings);
    }
    private static final int MAX_COUNT_TIME = 20;
    private int countTime = 0;

    public void lookPregnant(PlayerEntity user,ItemStack itemStack) {
        World world = user.getWorld();
        if (world.isClient) return;
        Entity targetEntity = RayCastUtils.getTargetEntity(world, user, 200.0);
        if(countTime >= MAX_COUNT_TIME){
            if (targetEntity instanceof AnimalEntity animalEntity) {
                if (!animalEntity.isBaby()) {
                    animalEntity.setBreedingAge(0); // 确保繁殖冷却已结束
                    animalEntity.setLoveTicks(600);
                    animalEntity.lovePlayer(user);
                    world.sendEntityStatus(animalEntity, (byte) 18);
                    itemStack.setDamage(itemStack.getDamage() + 1);
                }
            }else if (targetEntity instanceof VillagerEntity villager) {
                if (!villager.hasCustomer() && !villager.isBaby()) {
                    attemptVillagerBreeding(villager, world, user);
                    itemStack.setDamage(itemStack.getDamage() + 1);
                }
            }
            countTime = 0;
        }else {
            countTime++;
        }

    }

    private void attemptVillagerBreeding(VillagerEntity villager, World world, PlayerEntity user) {
        world.sendEntityStatus(villager, (byte) 12);
        if (!world.isClient) {
            VillagerEntity child = villager.createChild((ServerWorld) world, villager);
            if (child != null) {
                BlockPos spawnPos = villager.getBlockPos().add(world.random.nextInt(3) - 1, 0, world.random.nextInt(3) - 1);
                child.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0.0F, 0.0F);
                world.spawnEntity(child);
            }
        }
    }
}
