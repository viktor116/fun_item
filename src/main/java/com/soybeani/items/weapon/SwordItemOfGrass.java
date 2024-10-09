package com.soybeani.items.weapon;

import com.soybeani.items.ItemsRegister;
import com.soybeani.utils.CommonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/10/9 15:50
 * @description 草剑物品
 */
public class SwordItemOfGrass extends SwordItem {

    public SwordItemOfGrass(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    //回血
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(ItemsRegister.GRASS_SWORD2 == user.getStackInHand(hand).getItem()) {
            // 检查玩家的当前生命值
            if (user.getHealth() < user.getMaxHealth()) {
                ItemStack stackInHand = user.getStackInHand(hand);
                if(stackInHand.isDamageable()){
                    int currentDamage = stackInHand.getDamage();
                    stackInHand.setDamage(currentDamage + 1);
                    user.heal(2.0F);
                    // 添加粒子效果
                    CommonUtils.spawnParticlesAndPlaySound(world, user, ParticleTypes.HEART, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS,10, 1, 1.0, 1.0F, 1.0F);
                }
            }
        }
       return TypedActionResult.success(user.getStackInHand(hand));
    }

//    @Override
//    public ActionResult useOnBlock(ItemUsageContext context) {
//        PlayerEntity player = context.getPlayer();
//        if(player == null) return ActionResult.FAIL;
//        World world = player.getWorld();
//        BlockPos blockPos = context.getBlockPos();
//        BlockState blockState = world.getBlockState(blockPos);
//        Block block = blockState.getBlock();
//        if(world.isClient) return ActionResult.PASS;
//        //处理是草的逻辑
//        if(block == Blocks.SHORT_GRASS || block == Blocks.TALL_GRASS){
//            world.setBlockState(blockPos,Blocks.AIR.getDefaultState());
//            world.playSound((PlayerEntity) null,blockPos,SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS,1f,1f);
//            ItemStack stackInHand = player.getStackInHand(context.getHand());
//            if(stackInHand.isDamageable()){
//                int currentDamage = stackInHand.getDamage();
//                int maxDamage = stackInHand.getMaxDamage();
//                // 恢复一点耐久度
//                if (currentDamage > 0 || currentDamage < maxDamage) {
//                    stackInHand.setDamage(currentDamage - 1);
//                }
//            }
//        }
//        return ActionResult.PASS;
//    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if(state == Blocks.SHORT_GRASS.getDefaultState() || state == Blocks.TALL_GRASS.getDefaultState()){
            if(stack.isDamageable()){
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

    public static void EventRegister(ItemStack stack, World world, BlockState state, BlockPos pos) {
        if(state == Blocks.GRASS_BLOCK.getDefaultState()){
            BlockPos posUp = pos.up();
            if(stack.isDamageable()){
                int currentDamage = stack.getDamage();
                stack.setDamage(currentDamage + 1);
                world.setBlockState(posUp, Blocks.SHORT_GRASS.getDefaultState());
            }
        }
    }

}
