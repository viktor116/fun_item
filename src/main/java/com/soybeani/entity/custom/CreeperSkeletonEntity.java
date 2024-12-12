package com.soybeani.entity.custom;

import com.soybeani.config.InitValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

/**
 * @author soybean
 * @date 2024/12/9 15:48
 * @description
 */
public class CreeperSkeletonEntity extends SkeletonEntity {

    public static final EntityType<CreeperSkeletonEntity> CREEPER_SKELETON= Registry.register(
            Registries.ENTITY_TYPE,
            InitValue.id("creeper_skeleton"),
            EntityType.Builder.create(CreeperSkeletonEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.99f)
                    .eyeHeight(1.74f)
                    .vehicleAttachment(-0.7f)
                    .maxTrackingRange(8)
                    .build()
    );


    public CreeperSkeletonEntity(EntityType<? extends CreeperSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
        ItemStack itemStack2 = this.getProjectileType(itemStack);
        PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack2, pullProgress, itemStack);

        // 设置箭的特殊属性
        if (persistentProjectileEntity instanceof ArrowEntity arrow) {
            arrow.setDamage(arrow.getDamage() * 1.5); // 增加50%伤害
            arrow.addCommandTag("explosive_arrow"); // 标记为爆炸箭
        }

        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224, f, 1.6F, (float)(14 - this.getWorld().getDifficulty().getId() * 4));
        
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(persistentProjectileEntity);
    }
}
