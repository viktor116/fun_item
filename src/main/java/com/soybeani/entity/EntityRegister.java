package com.soybeani.entity;


import com.soybeani.config.InitValue;
import com.soybeani.entity.client.renderer.*;
import com.soybeani.entity.vehicle.FlyBoatEntity;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import com.soybeani.entity.vehicle.IceBoatEntity;
import com.soybeani.entity.vehicle.Su7CarEntity;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.ModelLoader;
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
        EntityRendererRegistry.register(FlyBoatEntity.FLY_BOAT, FlyBoatEntityRenderer::new);
//        BuiltinItemRendererRegistry.INSTANCE.register(ItemsRegister.LIGHTNING_SPYGLASS, new LightningSpyglassRenderer());
    }
}
