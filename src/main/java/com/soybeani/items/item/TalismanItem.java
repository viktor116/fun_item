package com.soybeani.items.item;

import com.soybeani.items.ItemsRegister;
import com.soybeani.utils.CommonUtils;
import com.soybeani.utils.DelayedTaskManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public static class StoreEffectDAO{
        private int level;
        public int remainingTicks;

        public StoreEffectDAO(int level, int remainingTicks) {
            this.setLevel(level);
            this.remainingTicks = remainingTicks;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
    public static final Map<UUID, StoreEffectDAO> activePurpleEffects = new HashMap<>();

    private static final Map<UUID, IronGolemEntity> playerGolemMap = new HashMap<>();
    private static final Map<UUID, Boolean> playerControllingMap = new HashMap<>();
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient || !(entity instanceof PlayerEntity player)) return;
        // 处理紫色符禄效果
        activePurpleEffects.entrySet().removeIf(entry -> {
            UUID playerId = entry.getKey();
            StoreEffectDAO value = entry.getValue();
            int remainingTicks = value.remainingTicks;

            if (remainingTicks <= 0) {
                return true; // 移除已结束的效果
            }

            PlayerEntity affectedPlayer = world.getServer().getPlayerManager().getPlayer(playerId);
            if (affectedPlayer == null || !affectedPlayer.isAlive()) {
                return true; // 移除无效玩家的效果
            }

            // 应用紫色符禄效果
            applyPurpleEffect(world, affectedPlayer,value.level);
            value.remainingTicks = remainingTicks -1;
            entry.setValue(value);
            return false;
        });
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void applyPurpleEffect(World world, PlayerEntity player,Integer level) {
        // 生成伤害性粒子环绕效果
        double radius = level + 1.0;
        double particleCount = 30;
        double angleIncrement = (2 * Math.PI) / particleCount;
        boolean hasHealedThisTick = false; // 用于追踪这一tick是否已经触发过治疗

        for(int i = 0; i < particleCount; i++) {
            double angle = i * angleIncrement;
            double x = player.getX() + radius * Math.cos(angle);
            double z = player.getZ() + radius * Math.sin(angle);

            // 生成紫色粒子
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.spawnParticles(
                    ParticleTypes.WITCH,
                    x,
                    player.getY() + 1,
                    z,
                    1,
                    0.0, 0.0, 0.0,
                    0.0
            );

            // 对周围实体造成伤害
            Box damageBox = new Box(
                    x - 0.5, player.getY(), z - 0.5,
                    x + 0.5, player.getY() + 2, z + 0.5
            );

            List<LivingEntity> entities = world.getEntitiesByClass(
                    LivingEntity.class,
                    damageBox,
                    entity -> entity != player && !entity.isSpectator()
            );

            for(LivingEntity entity : entities) {
                if(entity.damage(world.getDamageSources().magic(), 5.0f)) {
                    // 如果伤害成功应用且玩家未满血
                    if (!hasHealedThisTick && player.getHealth() < player.getMaxHealth()) {
                        // 恢复生命值
                        player.heal(1.0f);
                        hasHealedThisTick = true;

                        // 在玩家周围生成绿色治疗粒子
                        if (world instanceof ServerWorld) {
                            ServerWorld serverWorld2 = (ServerWorld) world;
                            serverWorld2.spawnParticles(
                                    ParticleTypes.HAPPY_VILLAGER,
                                    player.getX(),
                                    player.getY() + 1,
                                    player.getZ(),
                                    10,  // 粒子数量
                                    0.5, // X方向扩散
                                    0.5, // Y方向扩散
                                    0.5, // Z方向扩散
                                    0.0  // 速度
                            );
                        }
                    }
                }
            }
        }
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
                default -> {
                    return ActionResult.PASS;
                }
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
                case DARKGREEN -> handleGolemControl(player, world, stack);
//                case YELLOW_RED -> handleIgniteEffect(player, stack);
//                case NONE -> handleSwordQi(player, world, stack);
//                case PINK -> handleCinnabarWarding(player, stack);
//                case DARKGREEN -> handlePuppetMaster(player, world, stack);
//                case YELLOW -> handleGatlingGun(player, world, stack);
//                case SKYBLUE -> handleFlySword(player, world, stack);
//                case PURPLE -> handleBloodMagic(player, stack);
//                case GREY -> handleWitherPower(player, stack);
//                case BLACK_PURPLE -> handleDeathShadow(player, world, stack);
                default -> {
                    return TypedActionResult.pass(stack);
                }
            }
            return TypedActionResult.success(stack);
        }

        return super.use(world, player, hand);
    }

    //附身附魔
    private void handleGolemControl(PlayerEntity player, World world, ItemStack stack) {
        UUID playerId = player.getUuid();

        // 如果玩家已经在控制铁傀儡，右键点击会结束控制
        if (playerGolemMap.containsKey(playerId)) {
            // 获取并移除铁傀儡
            IronGolemEntity golem = playerGolemMap.get(playerId);
            if (golem != null && golem.isAlive()) {
                golem.remove(Entity.RemovalReason.DISCARDED);
            }
            playerGolemMap.remove(playerId);
            playerControllingMap.remove(playerId);

            // 恢复玩家状态
            player.setNoGravity(false);
            player.setInvulnerable(false);
            player.setVelocity(Vec3d.ZERO);

            player.sendMessage(Text.literal("已退出铁傀儡控制模式"), false);
        } else {
            // 生成铁傀儡
            IronGolemEntity golem = EntityType.IRON_GOLEM.create(world);
            if (golem != null) {
                // 设置铁傀儡位置和朝向
                golem.refreshPositionAndAngles(
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        player.getYaw(),
                        player.getPitch()
                );

                // 设置铁傀儡属性
                golem.setAiDisabled(true);
                golem.setCustomName(Text.literal(player.getName().getString() + "的铁傀儡"));
                golem.setCustomNameVisible(true);

                // 生成铁傀儡
                world.spawnEntity(golem);

                // 设置玩家状态
                player.setNoGravity(true);
                player.setInvulnerable(true);
                player.setVelocity(Vec3d.ZERO);

                // 记录关联
                playerGolemMap.put(playerId, golem);
                playerControllingMap.put(playerId, true);

                player.sendMessage(Text.literal("已进入铁傀儡控制模式"), false);
            }
        }
    }

    // 处理铁傀儡攻击
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient && attacker instanceof PlayerEntity player) {
            UUID playerId = player.getUuid();
            IronGolemEntity golem = playerGolemMap.get(playerId);

            if (golem != null && golem.isAlive()) {
                // 让铁傀儡执行攻击
                golem.tryAttack(target);

                // 播放攻击音效和动画
                World world = golem.getWorld();
                world.playSound(null,
                        golem.getX(),
                        golem.getY(),
                        golem.getZ(),
                        SoundEvents.ENTITY_IRON_GOLEM_ATTACK,
                        SoundCategory.HOSTILE,
                        1.0F,
                        1.0F
                );
                return true;
            }
        }
        return super.postHit(stack, target, attacker);
    }

    public static IronGolemEntity getControlledGolem(UUID playerId) {
        return playerGolemMap.get(playerId);
    }

    public static boolean isControllingGolem(UUID playerId) {
        return playerControllingMap.getOrDefault(playerId, false);
    }

    public static void cleanup(PlayerEntity player) {
        UUID playerId = player.getUuid();
        if (playerGolemMap.containsKey(playerId)) {
            IronGolemEntity golem = playerGolemMap.get(playerId);
            if (golem != null && golem.isAlive()) {
                golem.remove(Entity.RemovalReason.DISCARDED);
            }
            playerGolemMap.remove(playerId);
            playerControllingMap.remove(playerId);

            player.setNoGravity(false);
            player.setInvulnerable(false);
        }
    }

    // 清理方法更新
    private void handleLightningSpell(PlayerEntity player, BlockPos blockPos,ItemStack stack) { //雷法
        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(player.getWorld());
        player.setInvulnerable(true);
        lightning.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        player.getWorld().spawnEntity(lightning);
        if(!player.isInCreativeMode()){
            stack.decrement(1);
        }
        // 如果没有相关的任务正在进行，创建新任务
        String taskId = "lightning_invulnerability_" + player.getUuid();
        if (!DelayedTaskManager.hasTask(taskId)) {
            DelayedTaskManager.scheduleTask(taskId, 20, () -> {
                player.setInvulnerable(false);
            });
        }
    }

    private static void handleNatureHealing(PlayerEntity player, ItemStack stack,float healAmount) { //道法自然治疗
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

    private static void handleFlame(PlayerEntity player, BlockPos blockPos,ItemStack stack){
        World world = player.getWorld();
        if(world.getBlockState(blockPos.up()) == Blocks.AIR.getDefaultState()){
            world.setBlockState(blockPos.up(), Blocks.FIRE.getDefaultState());
            if(!player.isInCreativeMode()){
                stack.decrement(1);
            }
        }
    }
    private static void handleFlameToEntity(PlayerEntity player,Entity entity,ItemStack stack,int tick){
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

    //剑气
    public static void spawnSwordSlashParticles(PlayerEntity player, World world,ItemStack stack, float damage) {
        // 获取玩家的位置和视线方向
        Vec3d pos = player.getEyePos();
        Vec3d lookVec = player.getRotationVector();

        // 剑气的基本参数
        double range = 50.0; // 剑气的范围
        double width = 5.0; // 剑气的宽度
        int particleCount = 50; // 粒子数量

        if(!player.isInCreativeMode()){
            ItemStack offHandStack = player.getOffHandStack();
            if(offHandStack.getItem() instanceof TalismanItem talismanItem){
                if(talismanItem.getType() == Type.NONE){
                    offHandStack.decrement(1);

                }
            }
        }
        world.playSound(null,player.getBlockPos(),SoundEvents.ITEM_TRIDENT_THROW.value(),SoundCategory.PLAYERS,1, 0.5f + (float) Math.random());
        // 创建一个扇形的剑气效果
        for (int i = 0; i < particleCount; i++) {
            // 计算粒子的位置
            double progress = i / (double) particleCount;
            Vec3d rotatedVec = new Vec3d(
                    lookVec.x,
                    lookVec.y,
                    lookVec.z
            );

            // 添加一些随机性
            double spread = width * (Math.random() - 0.5) * 0.2;
            Vec3d particlePos = pos.add(
                    rotatedVec.x * range * progress + rotatedVec.z * spread,
                    rotatedVec.y * range * progress + (Math.random() - 0.5) * 0.2,
                    rotatedVec.z * range * progress - rotatedVec.x * spread
            );

            // 发送粒子包给客户端
            ((ServerWorld) world).spawnParticles(
                    ParticleTypes.SWEEP_ATTACK, // 使用原版的剑气粒子
                    particlePos.x, particlePos.y, particlePos.z,
                    1, // 每个位置生成的粒子数量
                    0, 0, 0, // 速度扩散
                    0.1 // 速度
            );
        }

        // 检测伤害范围内的实体
        Box damageBox = player.getBoundingBox().stretch(lookVec.multiply(range)).expand(width);
        List<Entity> entities = world.getOtherEntities(player, damageBox);

        for (Entity target : entities) {
            if (target instanceof LivingEntity) {
                // 检查实体是否在剑气的扇形范围内
                Vec3d directionToTarget = target.getPos().subtract(pos).normalize();
                double dotProduct = lookVec.dotProduct(directionToTarget);
                double angleToTarget = Math.acos(dotProduct);

                if (angleToTarget <= Math.PI / 4) { // 45度范围内
                    // 造成伤害
                    target.damage(target.getDamageSources().magic(), damage);

                    // 额外的击中特效
                    ((ServerWorld) world).spawnParticles(
                            ParticleTypes.CRIT,
                            target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                            10, // 数量
                            0.2, 0.2, 0.2, // 扩散范围
                            0.5 // 速度
                    );
                }
            }
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
            double baseDamage = player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            ItemStack heldItem = player.getStackInHand(hand);
            if (heldItem.getItem() instanceof SwordItem) {
                ToolMaterial material = ((SwordItem) heldItem.getItem()).getMaterial();
                baseDamage += material.getAttackDamage();
            }
            switch (type) {
                case BLUE:
                    talismanItem.handleLightningSpell(player, entity.getBlockPos(), offHandStack);
                    break;
                case GREEN:
                    // 计算最终伤害(考虑暴击等)
                    float cooldown = player.getAttackCooldownProgress(0.5f);
                    float finalDamage = (float) baseDamage * (0.2f + cooldown * cooldown * 0.8f);
                    talismanItem.handleNatureHealing(player, offHandStack, finalDamage);
                    break;
                case YELLOW_RED:
                    talismanItem.handleFlameToEntity(player, entity, offHandStack, 100);
                    break;
                case PINK:
                    if (entity instanceof LivingEntity target) {
                        float extraDamage =(float) (target instanceof HostileEntity ? baseDamage + 10.0f : baseDamage + 0f ); // 对亡灵额外伤害
                        target.damage(target.getDamageSources().magic(), extraDamage);

                        // 击中特效
                        if (world instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(
                                    ParticleTypes.CRIMSON_SPORE,
                                    target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                                    50, 1, 1, 1,
                                    0.1
                            );
                        }

                        // 播放音效
                        world.playSound(null, target.getBlockPos(),
                                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                                SoundCategory.PLAYERS, 1.0f, 1.2f);

                        if (!player.isInCreativeMode()) {
                            offHandStack.decrement(1);
                        }
                    }
                    break;
                case GREY: //凋零之力
                    if (entity instanceof LivingEntity target) {
                        float extraDamage =(float) (target instanceof AbstractSkeletonEntity ? baseDamage + 20.0f : baseDamage + 0f ); //
                        target.damage(target.getDamageSources().magic(), extraDamage);

                        if(((LivingEntity) entity).isDead()){
                            // 击中特效
                            if (world instanceof ServerWorld serverWorld) {
                                serverWorld.spawnParticles(
                                        ParticleTypes.SMOKE,
                                        target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                                        50, 0.2, 0.2, 0.2,
                                        0.1
                                );
                            }

                            // 播放音效
                            world.playSound(null, target.getBlockPos(),
                                    SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                                    SoundCategory.PLAYERS, 1.0f, 1.2f);
                            if(target instanceof AbstractSkeletonEntity){
                                ItemEntity itemEntity = new ItemEntity(target.getWorld(), target.getX()-0.5+Math.random(), target.getY()+1, target.getZ()-0.5+Math.random(), ItemsRegister.TALISMAN_GREY.getDefaultStack());
                                world.spawnEntity(itemEntity);
                            }
                            if (!player.isInCreativeMode()) {
                                offHandStack.decrement(1);
                            }
                        }
                    }
                    break;
                case BLACK_PURPLE:
                    if (entity instanceof LivingEntity target) {
                        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1200, 1));
                        if (world instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(
                                    ParticleTypes.SMOKE,
                                    target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                                    200, 1, 1, 1,
                                    0.2
                            );
                        }
                        world.playSound(null, target.getBlockPos(),
                                SoundEvents.ENTITY_SKELETON_DEATH,
                                SoundCategory.PLAYERS, 1.0f, 1.2f);
                        if (!player.isInCreativeMode()) {
                            offHandStack.decrement(1);
                        }
                    }
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
