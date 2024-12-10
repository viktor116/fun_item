package com.soybeani.block;

import com.soybeani.block.client.renderer.*;
import com.soybeani.block.custom.*;
import com.soybeani.block.entity.*;
import com.soybeani.config.InitValue;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/24 12:10
 * @description
 */
public class ModBlock {
    public static final Block AIR_ICE =  register(new AirIceBlock(AbstractBlock.Settings.create().slipperiness(0.98F).ticksRandomly().strength(0.5F).nonOpaque().sounds(BlockSoundGroup.GLASS)), "air_ice_0",true);
    public static final Block SUPER_SLIME_BLOCK = register(new SuperSlimeBlock(AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).slipperiness(0.8f).sounds(BlockSoundGroup.SLIME).nonOpaque()),"super_slime_block",true);
    public static final Block SUPER_SLIME_BLOCK_MAX = register(new SuperSlimeBlockMax(AbstractBlock.Settings.create().mapColor(MapColor.RED).slipperiness(0.8f).sounds(BlockSoundGroup.SLIME).nonOpaque()),"super_slime_block_max",true);
    public static final Block TT_BLOCK = register(new TTBlock(AbstractBlock.Settings.copy(Blocks.TNT)), "tt_block",true);

    public static final EntityType<TTEntity> TT_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "tt_entity"),
            FabricEntityTypeBuilder.<TTEntity>create(SpawnGroup.MISC, TTEntity::new)
                    .dimensions(EntityDimensions.fixed(0.98f, 0.98f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static final Block COW_PLANT = register("cow_plant", new CowPlantBlock(AbstractBlock.Settings.create()
            .nonOpaque()
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)),false);

    public static final BlockEntityType<CowPlantBlockEntity> COW_PLANT_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "cow_plant"),
            BlockEntityType.Builder.create(CowPlantBlockEntity::new, ModBlock.COW_PLANT).build(null)
    );

    public static final Block PIG_PLANT = register("pig_plant", new PigPlantBlock(AbstractBlock.Settings.create()
            .nonOpaque()
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)),false);

    public static final BlockEntityType<PigPlantBlockEntity> PIG_PLANT_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "pig_plant"),
            BlockEntityType.Builder.create(PigPlantBlockEntity::new, ModBlock.PIG_PLANT).build(null)
    );

    public static final Block ZOMBIE_PLANT = register("zombie_plant", new ZombiePlantBlock(AbstractBlock.Settings.create()
            .nonOpaque()
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)),false);

    public static final BlockEntityType<ZombiePlantBlockEntity> ZOMBIE_PLANT_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "zombie_plant"),
            BlockEntityType.Builder.create(ZombiePlantBlockEntity::new, ModBlock.ZOMBIE_PLANT).build(null)
    );

    public static final Block DIAMOND_ORE_PLANT = register("diamond_ore_plant", new DiamondOrePlantBlock(AbstractBlock.Settings.create()
            .nonOpaque()
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)),false);

    public static final BlockEntityType<DiamondOrePlantEntity> DIAMOND_ORE_PLANT_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "diamond_ore_plant"),
            BlockEntityType.Builder.create(DiamondOrePlantEntity::new, ModBlock.DIAMOND_ORE_PLANT).build(null)
    );

    public static final Block DIAMOND_SWORD_PLANT = register("diamond_sword_plant", 
        new DiamondSwordPlantBlock(AbstractBlock.Settings.create()
            .nonOpaque()
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)), 
        false);

    public static final BlockEntityType<DiamondSwordPlantEntity> DIAMOND_SWORD_PLANT_TYPE = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Identifier.of(InitValue.MOD_ID, "diamond_sword_plant"),
        BlockEntityType.Builder.create(DiamondSwordPlantEntity::new, ModBlock.DIAMOND_SWORD_PLANT).build(null)
    );

    public static void initialize(){

    }
    public static void initializeClient(){
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlock.AIR_ICE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlock.SUPER_SLIME_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlock.SUPER_SLIME_BLOCK_MAX, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlock.TT_BLOCK, RenderLayer.getCutout());
        EntityRendererRegistry.register(TT_ENTITY, TTEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), COW_PLANT);
        BlockEntityRendererFactories.register(COW_PLANT_TYPE, CowPlantBlockRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), PIG_PLANT);
        BlockEntityRendererFactories.register(PIG_PLANT_TYPE, PigPlantBlockRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ZOMBIE_PLANT);
        BlockEntityRendererFactories.register(ZOMBIE_PLANT_TYPE, ZombiePlantBlockRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), DIAMOND_ORE_PLANT);
        BlockEntityRendererFactories.register(DIAMOND_ORE_PLANT_TYPE, DiamondOrePlantBlockRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), DIAMOND_SWORD_PLANT);
        BlockEntityRendererFactories.register(DIAMOND_SWORD_PLANT_TYPE, DiamondSwordPlantRenderer::new);
    }

    public static Block register(Block block, String id,boolean shouldRegisterItem){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, itemID, blockItem);
        }
        return Registry.register(Registries.BLOCK, itemID, block);
    }

    public static Block register(String id,Block block, boolean shouldRegisterItem){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, itemID, blockItem);
        }
        return Registry.register(Registries.BLOCK, itemID, block);
    }
}
