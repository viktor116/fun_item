package com.soybeani.items.item;

import com.soybeani.config.InitValue;
import com.soybeani.entity.client.renderer.GatlingRenderer;
import com.soybeani.entity.custom.BulletEntity;
import com.soybeani.items.ItemsRegister;
import com.soybeani.sound.SoundRegister;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author soybean
 * @date 2024/12/27 14:51
 * @description
 */
public class GatlingItem extends RangedWeaponItem implements GeoItem {
    private static final RawAnimation ACTIVATE_SHOOT = RawAnimation.begin().then("shoot", Animation.LoopType.LOOP);
    private static final RawAnimation ACTIVATE_IDLE= RawAnimation.begin().then("idle", Animation.LoopType.LOOP);

    private boolean isShooting = false;
    private static final int SHOOT_INTERVAL = 5; // 射击间隔（tick）
    private int shootCooldown = 0;

    // 添加声音控制相关变量
    private static final int SOUND_DURATION = 140; // 7秒 = 140 ticks
    private int soundTimer = 0;
    private boolean isSoundPlaying = false;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final Predicate<ItemStack> BULLET_PROJECTILES = (stack) -> {
//        return stack.isIn(ItemTags.ARROWS);
        return stack.getItem() instanceof BulletItem || stack.isIn(ItemTags.ARROWS);
    };
    public static final int RANGE = 100;
    public GatlingItem(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BULLET_PROJECTILES;
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        // 实现射击逻辑
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, speed, divergence);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoItem>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<GeoItem> animationState) {
        if(!this.isShooting){
            animationState.getController().setAnimation(ACTIVATE_IDLE);
        }else{
            animationState.getController().setAnimation(ACTIVATE_SHOOT);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            // 检查是否正在使用物品
            if (player.isUsingItem() && player.getActiveItem() == stack) {
                this.isShooting = true;
                // 在服务器端处理射击逻辑
                if (!world.isClient && shootCooldown <= 0) {
                    shootArrow(world, player, stack);

                    // 声音播放逻辑
                    if (!isSoundPlaying) {
                        world.playSound(null,
                                player.getBlockPos(),
                                SoundRegister.GATLING_GUN,
                                SoundCategory.PLAYERS,
                                1f,
                                0.5f + world.random.nextFloat()
                        );
                        isSoundPlaying = true;
                        soundTimer = SOUND_DURATION;
                    }

                    shootCooldown = SHOOT_INTERVAL;
                }
            } else {
                this.isShooting = false;
                // 停止使用时重置声音状态
                isSoundPlaying = false;
                soundTimer = 0;
            }

            // 更新射击冷却
            if (shootCooldown > 0) {
                shootCooldown--;
            }

            // 更新声音计时器
            if (isSoundPlaying) {
                if (soundTimer > 0) {
                    soundTimer--;
                } else {
                    isSoundPlaying = false; // 声音播放完毕
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void shootArrow(World world, PlayerEntity player, ItemStack weaponStack) {
        ItemStack bulletStack = player.getProjectileType(weaponStack);
        if (!bulletStack.isEmpty() && !world.isClient) {
            if (bulletStack.getItem() instanceof BulletItem bulletItem) {
                BulletEntity.Type type;
                if(bulletItem == ItemsRegister.COPPER_BULLET){
                    type = BulletEntity.Type.COPPER;
                }else{
                    type = BulletEntity.Type.IRON;
                }
                bulletItem.setType(type);
                PersistentProjectileEntity projectile = bulletItem.createBullet(world, bulletStack, player,bulletStack);
                projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 5.0F, world.random.nextFloat());
                projectile.setOwner(player);
                projectile.setDamage(projectile.getDamage() * 1.5);

                world.spawnEntity(projectile);
                // 消耗箭矢
                if (!player.getAbilities().creativeMode) {
                    bulletStack.decrement(1);
                    if (bulletStack.isEmpty()) {
                        player.getInventory().removeOne(bulletStack);
                    }
                }

                player.incrementStat(Stats.USED.getOrCreateStat(this));
            }else if(bulletStack.getItem() instanceof ArrowItem arrowItem){
                PersistentProjectileEntity projectile = arrowItem.createArrow(world, bulletStack, player,bulletStack);
                projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 3.0F, world.random.nextFloat());
                projectile.setCritical(true);
                projectile.setOwner(player);
                projectile.setDamage(projectile.getDamage() * 1.5);

                world.spawnEntity(projectile);
                world.playSound(null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_ARROW_SHOOT,
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F)
                );
                // 消耗箭矢
                if (!player.getAbilities().creativeMode) {
                    bulletStack.decrement(1);
                    if (bulletStack.isEmpty()) {
                        player.getInventory().removeOne(bulletStack);
                    }
                }

                player.incrementStat(Stats.USED.getOrCreateStat(this));
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean bl = !player.getProjectileType(itemStack).isEmpty();
        if (!player.isInCreativeMode() && !bl) {
            return TypedActionResult.fail(itemStack);
        } else {
            player.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        this.isShooting = false;
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GatlingRenderer renderer;

            @Override
            public @Nullable BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new GatlingRenderer();
                return this.renderer;
            }
        });
    }
}
