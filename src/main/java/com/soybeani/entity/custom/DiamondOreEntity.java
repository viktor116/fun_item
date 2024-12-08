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

public class DiamondOreEntity extends AnimalEntity {

    public static final EntityType<DiamondOreEntity> DIAMOND_ORE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "diamond_ore_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DiamondOreEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1f))
                    .build()
    );

    private int hurtTime;
    private float prevHurtTime;

    protected DiamondOreEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0) // 更高的生命值
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2) // 稍慢的移动速度
                .add(EntityAttributes.GENERIC_JUMP_STRENGTH, 0.5);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerable()) {
            return false;
        }
        this.hurtTime = 10;
        this.prevHurtTime = 10;
        return super.damage(source, amount);
    }

    public int getHurtTime() {
        return this.hurtTime;
    }

    @Override
    public void tick() {
        this.prevHurtTime = this.hurtTime;
        if (this.hurtTime > 0) {
            this.hurtTime--;
        }
        this.setYaw(MathHelper.wrapDegrees(this.getYaw()));
        super.tick();
    }

    public float getHurtProgress(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevHurtTime, this.hurtTime) / 10.0F;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.DIAMOND); // 使用钻石作为繁殖物品
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (this.getBreedingAge() == 0 && this.isReadyToBreed()) {
            if (this.isBreedingItem(itemStack)) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                return ActionResult.success(this.getWorld().isClient);
            }
        }

        if (this.isBaby() && itemStack.isOf(Items.DIAMOND)) {
            this.growUp((int)(this.getBreedingAge()), true);
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
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

    public void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        this.getWorld().addParticle(
                ParticleTypes.HAPPY_VILLAGER,
                this.getX(),
                this.getY() + 0.5,
                this.getZ(),
                0.0, 0.0, 0.0
        );
    }

    @Override
    public void dropLoot(DamageSource source, boolean causedByPlayer) {
        super.dropLoot(source, causedByPlayer);
        int count = this.random.nextBetween(1, 3);
        this.dropItem(Items.DIAMOND, count);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new TemptGoal(this, 1.2,
                Ingredient.ofItems(Items.DIAMOND), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return DIAMOND_ORE.create(world);
    }
} 