package com.soybeani.entity.custom;

import com.soybeani.config.InitValue;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

/**
 * @author soybean
 * @date 2024/11/6 14:54
 * @description
 */
public class PurpleLightningEntity extends LightningEntity {
    public static PurpleLightningEntity INSTANCE;

    public static final EntityType<PurpleLightningEntity> PURPLE_LIGHTNING = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "purple_lightning"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PurpleLightningEntity::new)
                    .dimensions(EntityDimensions.fixed(0.0f, 0.0f))
                    .trackRangeBlocks(300)
                    .trackedUpdateRate(Integer.MAX_VALUE)
                    .build()
    );
    public PurpleLightningEntity(EntityType<? extends LightningEntity> entityType, World world) {
        super(entityType, world);
        INSTANCE = this;
    }
}
