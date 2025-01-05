package com.soybeani.entity.custom;

import com.soybeani.config.InitValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/9 15:21
 * @description
 */

public class ZombiePregnantEntity extends ZombieEntity {
    public static final EntityType<ZombiePregnantEntity> SKELETON_ZOMBIE = Registry.register(
            Registries.ENTITY_TYPE,
            InitValue.id("zombie_pregnant"),
            EntityType.Builder.create(ZombiePregnantEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.95f)
                    .eyeHeight(1.74f)
                    .passengerAttachments(2.0125f)
                    .vehicleAttachment(-0.7f)
                    .maxTrackingRange(8)
                    .build()
    );

    public ZombiePregnantEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    // 设置实体属性，包括更快的移动速度
    public static DefaultAttributeContainer.Builder createZombieAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS);
    }
}
