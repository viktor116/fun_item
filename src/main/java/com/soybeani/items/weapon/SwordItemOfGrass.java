package com.soybeani.items.weapon;

import com.soybeani.items.ItemsRegister;
import com.soybeani.utils.CommonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author soybean
 * @date 2024/10/9 15:50
 * @description 草剑物品
 */
public class SwordItemOfGrass extends SwordItem {

    public SwordItemOfGrass(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    //回血(右键)
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        boolean isGrassSword = ItemsRegister.GRASS_SWORD2 == user.getStackInHand(hand).getItem() || ItemsRegister.WHEAT_SWORD == user.getStackInHand(hand).getItem();
        if (isGrassSword) {
            // 检查玩家的当前生命值
            if (user.getHealth() < user.getMaxHealth()) {
                ItemStack stackInHand = user.getStackInHand(hand);
                if (stackInHand.isDamageable()) {
                    int currentDamage = stackInHand.getDamage();
                    int maxDamage = stackInHand.getMaxDamage();
                    if (!user.isInCreativeMode()) {
                        currentDamage++;
                        stackInHand.setDamage(currentDamage);
                        if (currentDamage >= maxDamage) {
                            user.setStackInHand(hand, ItemStack.EMPTY);
                            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                                    SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
                        }
                    }
                    user.heal(2.0F);
                    // 添加粒子效果
                    CommonUtils.spawnParticlesAndPlaySound(world, user, ParticleTypes.HEART, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 10, 1, 1.0, 1.0F, 1.0F);
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    //破坏草回耐久
    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!miner.getWorld().isClient) {
            boolean isHeal = false;
            if ((state == Blocks.SHORT_GRASS.getDefaultState() || state == Blocks.TALL_GRASS.getDefaultState()) && stack.isDamageable()) {
               isHeal = true;
            }
            if((state.getBlock() == Blocks.WHEAT && miner.getMainHandStack().getItem() == ItemsRegister.WHEAT_SWORD) && stack.isDamageable()){
                isHeal = true;
            }
            if(isHeal){
                int currentDamage = stack.getDamage();
                int maxDamage = stack.getMaxDamage();
                // 恢复一点耐久度
                if (currentDamage > 0 || currentDamage < maxDamage) {
                    stack.setDamage(currentDamage - 1);
                }
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    //种草
    public static void EventRegister(PlayerEntity player, ItemStack stack, World world, BlockState state, BlockPos pos) {
        if (player.getWorld().isClient) return;
        ItemStack mainHandStack = player.getMainHandStack();
        boolean isWheatSword = mainHandStack.getItem() == ItemsRegister.WHEAT_SWORD;
        boolean isGrassSword = mainHandStack.getItem() == ItemsRegister.GRASS_SWORD || mainHandStack.getItem() == ItemsRegister.GRASS_SWORD2 || isWheatSword;
        BlockPos posUp = pos.up();
        if (stack.isDamageable() && world.getBlockState(posUp).isAir()) {
            int currentDamage = stack.getDamage();
            int maxDamage = stack.getMaxDamage();
            boolean hasPlace = false;
            if (state == Blocks.GRASS_BLOCK.getDefaultState() && isGrassSword) {
                hasPlace = true;
                world.setBlockState(posUp, Blocks.SHORT_GRASS.getDefaultState());
            } else if ((state == Blocks.FARMLAND.getDefaultState() || state.getBlock() == Blocks.FARMLAND) && isWheatSword) {
                hasPlace = true;
                world.setBlockState(posUp, Blocks.WHEAT.getDefaultState());
            }
            if(hasPlace){
                world.playSound(player, posUp, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!player.isInCreativeMode())  {
                    currentDamage++;
                    stack.setDamage(currentDamage);
                    if (currentDamage >= maxDamage) {
                        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
                    }
                }
            }
        }
    }

    //小麦剑攻击生物随机掉落
    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean isWheatSword = stack.getItem() == ItemsRegister.WHEAT_SWORD;
        if (isWheatSword && !attacker.getWorld().isClient) {
            Random random = CommonUtils.getRandom();
            float chance = random.nextFloat();
            if (chance < 0.6667f) { // 2/3 的概率会掉落物品
                ItemStack dropStack;
                if (random.nextBoolean()) {
                    dropStack = new ItemStack(Items.WHEAT_SEEDS);
                } else {
                    dropStack = new ItemStack(Items.WHEAT);
                }
                //掉落
                target.dropStack(dropStack);
                target.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                        ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
        super.postDamageEntity(stack, target, attacker);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.FAIL;
        World world = player.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (world.isClient) return ActionResult.PASS;
        //处理是草的逻辑
        if (block == Blocks.WHEAT && player.getMainHandStack().getItem() == ItemsRegister.WHEAT_SWORD) {
            BlockState currentState = world.getBlockState(blockPos);
            IntProperty ageProperty = CropBlock.AGE;
            int currentAge = currentState.get(ageProperty);
            int maxAge = ((CropBlock) Blocks.WHEAT).getMaxAge();

            if (currentAge < maxAge) {
                // 催熟效果：增加作物年龄
                int newAge = Math.min(currentAge + 1, maxAge);
                world.setBlockState(blockPos, currentState.with(ageProperty, newAge), 2);

                // 播放音效
                world.playSound(null, blockPos, SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1f, 1f);

                // 处理物品耐久度
                ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
                if (!player.isCreative()) {
                    int currentDamage = stack.getDamage();
                    int maxDamage = stack.getMaxDamage();
                    currentDamage++;
                    stack.setDamage(currentDamage);

                    if (currentDamage >= maxDamage) {
                        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS,
                                0.8F, 0.8F + world.random.nextFloat() * 0.4F);
                    }
                }

                // 可选：添加粒子效果
                world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                        blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5,
                        0.0D, 0.0D, 0.0D);

            }
        }
        return ActionResult.PASS;
    }
}
