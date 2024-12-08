package com.soybeani.entity.custom;

import com.soybeani.config.InitValue;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MinecartEntity extends AnimalEntity {

    public static final EntityType<MinecartEntity> MINECART = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "minecart_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MinecartEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1f))
                    .build()
    );

    private int hurtTime;

    protected MinecartEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0) // 添加生命值
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_JUMP_STRENGTH, 0.5);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerable()) {
            return false;
        }
        this.hurtTime = 10;
        return super.damage(source, amount);
    }

    public int getHurtTime() {
        return this.hurtTime;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hurtTime > 0) {
            this.hurtTime--;
        }
        
        // 规范化 yaw 值到 -180 到 180 度之间
        this.setYaw(MathHelper.wrapDegrees(this.getYaw()));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.IRON_INGOT);
    }

    @Override
    public boolean isReadyToBreed() {
        return this.getBreedingAge() == 0 && !this.isBaby();  // 确保是成年且不在冷却中
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        MinecartEntity baby = (MinecartEntity) this.createChild(world, other);
        if (baby != null) {
            // 设置婴儿年龄为负数（表示成长时间）
            baby.setBaby(true);
            baby.setBreedingAge(-6000);
            baby.setPosition(this.getX(), this.getY(), this.getZ());

            // 重置父母的繁殖冷却时间
            this.setBreedingAge(600);
            ((MinecartEntity)other).setBreedingAge(600);

            world.spawnEntity(baby);
        }
        // 繁殖效果
        world.spawnParticles(ParticleTypes.HEART,
                this.getX(), this.getY() + 0.5, this.getZ(),
                7, 0.3, 0.3, 0.3, 0.0);
    }

    // 成长相关方法
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // 检查是否在繁殖冷却中
        if (this.getBreedingAge() == 0 && this.isReadyToBreed()) {
            if (this.isBreedingItem(itemStack)) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                return ActionResult.success(this.getWorld().isClient);
            }
        }

        // 如果是幼年且手持小麦，则加速生长
        if (this.isBaby() && itemStack.isOf(Items.IRON_INGOT)) {
            // 加速生长
            this.growUp((int)(this.getBreedingAge()), true);
            // 消耗物品
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            // 生成进食粒子效果
            this.getWorld().addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    this.getX(),
                    this.getY() + 0.5,
                    this.getZ(),
                    0.0, 0.0, 0.0
            );
            return ActionResult.success(this.getWorld().isClient);
        }

        return super.interactMob(player, hand);
    }

    // 添加一个辅助方法来处理进食
    public void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        // 生成进食粒子效果
        this.getWorld().addParticle(
                ParticleTypes.HAPPY_VILLAGER,
                this.getX(),
                this.getY() + 0.5,
                this.getZ(),
                0.0, 0.0, 0.0
        );
    }

    // 掉落物相关
    @Override
    public void dropLoot(DamageSource source, boolean causedByPlayer) {
        super.dropLoot(source, causedByPlayer);
        this.dropItem(Items.MINECART, 1);
    }

    // AI目标
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new TemptGoal(this, 1.2,
                Ingredient.ofItems(Items.IRON_INGOT), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }


    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return MINECART.create(world);
    }
}
