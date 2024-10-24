package com.soybeani.entity;


import com.soybeani.config.InitValue;
import com.soybeani.entity.client.renderer.Ice2BoatEntityRenderer;
import com.soybeani.entity.client.renderer.IceBoatEntityRenderer;
import com.soybeani.entity.client.renderer.Su7CarRenderer;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import com.soybeani.entity.vehicle.IceBoatEntity;
import com.soybeani.entity.vehicle.Su7CarEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/19 10:48
 * @description
 */
public class EntityRegister {
    public static final EntityType<Su7CarEntity> SU7 = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(InitValue.MOD_ID, "su7"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE,Su7CarEntity::new) .dimensions(EntityDimensions.fixed(1.5f, 1.5f)).build());
    public static void initialize(){
        FabricDefaultAttributeRegistry.register(SU7,Su7CarEntity.createAttributes());

    }

    public static void initializeClient(){
        EntityRendererRegistry.register(EntityRegister.SU7, Su7CarRenderer::new);
        EntityRendererRegistry.register(IceBoatEntity.ICE_BOAT, IceBoatEntityRenderer::new);
        EntityRendererRegistry.register(Ice2BoatEntity.ICE2_BOAT, Ice2BoatEntityRenderer::new);
    }
}
