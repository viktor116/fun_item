package com.soybeani.items.item;

import com.soybeani.Fun_Item;
import com.soybeani.utils.CommonUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/19 10:00
 * @description
 */
public class TalismanItem extends Item {

    public Type type;
    public TalismanItem(Settings settings,Type type) {
        super(settings);
        this.type = type;
    }

    public Type getType(){
        return type;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        BlockPos blockPos = context.getBlockPos();
        ItemStack stack = player.getStackInHand(context.getHand());
        if (!player.getWorld().isClient) {
            switch (type) {
                case BLUE -> handleLightningSpell(player, blockPos, stack);
                case YELLOW_RED -> handleFlame(player, blockPos, stack);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            switch (type) {
                case GREEN -> handleNatureHealing(player, stack ,0.5F);

//                case YELLOW_RED -> handleIgniteEffect(player, stack);
//                case NONE -> handleSwordQi(player, world, stack);
//                case PINK -> handleCinnabarWarding(player, stack);
//                case DARKGREEN -> handlePuppetMaster(player, world, stack);
//                case YELLOW -> handleGatlingGun(player, world, stack);
//                case SKYBLUE -> handleFlySword(player, world, stack);
//                case PURPLE -> handleBloodMagic(player, stack);
//                case GREY -> handleWitherPower(player, stack);
//                case BLACK_PURPLE -> handleDeathShadow(player, world, stack);
            }
            return TypedActionResult.success(stack);
        }

        return super.use(world, player, hand);
    }



    private boolean invulnerabilityTaskScheduled = false;
    private int tickCounter = 0;

    private void handleLightningSpell(PlayerEntity player, BlockPos blockPos,ItemStack stack) { //雷法
        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(player.getWorld());
        player.setInvulnerable(true);
        lightning.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        player.getWorld().spawnEntity(lightning);
        if(!player.isInCreativeMode()){
            stack.decrement(1);
        }
        // 如果没有安排任务，则注册一个新的tick事件
        if (!invulnerabilityTaskScheduled) {
            ServerTickEvents.END_SERVER_TICK.register(server -> {
                tickCounter++;
                if (tickCounter >= 20) { // 20 ticks后
                    tickCounter = 0;
                    player.setInvulnerable(false);
                }
            });
            invulnerabilityTaskScheduled = true;
        }
    }

    private void handleNatureHealing(PlayerEntity player, ItemStack stack,float healAmount) { //道法自然治疗
        if(player.getHealth() >= player.getMaxHealth()) return;
        player.heal(healAmount);
        // 播放治疗音效
        player.getWorld().playSound(
                null, // 如果是null，所有玩家都能听到
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ENTITY_PLAYER_LEVELUP,
                SoundCategory.PLAYERS,
                0.5F, // 音量
                1.0F + (player.getWorld().random.nextFloat() - player.getWorld().random.nextFloat()) * 0.4F // 音高
        );

        // 生成粒子效果
        if (player.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) player.getWorld();
            CommonUtils.spawnSelfParticle(serverWorld,player, ParticleTypes.HAPPY_VILLAGER);
        }
        if(!player.isInCreativeMode()){
            stack.decrement(1);
        }
    }

    private void handleFlame(PlayerEntity player, BlockPos blockPos,ItemStack stack){
        World world = player.getWorld();
        if(world.getBlockState(blockPos.up()) == Blocks.AIR.getDefaultState()){
            world.setBlockState(blockPos.up(), Blocks.FIRE.getDefaultState());
            if(!player.isInCreativeMode()){
                stack.decrement(1);
            }
        }
    }
    private void handleFlameToEntity(PlayerEntity player,Entity entity,ItemStack stack,int tick){
        entity.setFireTicks(tick);
        if(!player.isInCreativeMode()){
            stack.decrement(1);
        }
        // 生成粒子效果
        if (player.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) player.getWorld();
            CommonUtils.spawnSelfParticle(serverWorld,player, ParticleTypes.FLAME);
        }
    }

    public static void EventRegister(PlayerEntity player, Hand hand, World world, Entity entity, HitResult hitResult) {
        if(world.isClient){
            return;
        }
        ItemStack offHandStack = player.getOffHandStack();
        ItemStack mainHandStack = player.getMainHandStack();
        if (offHandStack.getItem() instanceof TalismanItem talismanItem && mainHandStack.getItem() == Items.WOODEN_SWORD) {
            Type type = talismanItem.getType();
            switch (type) {
                case BLUE:
                    talismanItem.handleLightningSpell(player, entity.getBlockPos(), offHandStack);
                    break;
                case GREEN:
                    // 获取玩家的基础攻击伤害
                    double baseDamage = player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    ItemStack heldItem = player.getStackInHand(hand);
                    if (heldItem.getItem() instanceof SwordItem) {
                        ToolMaterial material = ((SwordItem) heldItem.getItem()).getMaterial();
                        baseDamage += material.getAttackDamage();
                    }
                    // 计算最终伤害(考虑暴击等)
                    float cooldown = player.getAttackCooldownProgress(0.5f);
                    float finalDamage = (float) baseDamage * (0.2f + cooldown * cooldown * 0.8f);
                    talismanItem.handleNatureHealing(player, offHandStack, finalDamage);
                case YELLOW_RED:
                    talismanItem.handleFlameToEntity(player, entity, offHandStack, 100);
            }
        }
    }


    public enum Type{
        NONE,
        BLACK_PURPLE,
        BLUE,
        DARKGREEN,
        GREEN,
        GREY,
        PINK,
        PURPLE,
        SKYBLUE,
        YELLOW,
        YELLOW_RED
    }
}
