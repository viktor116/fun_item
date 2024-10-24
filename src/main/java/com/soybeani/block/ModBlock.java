package com.soybeani.block;

import com.soybeani.block.custom.AirIceBlock;
import com.soybeani.config.InitValue;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
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
    public static void initialize(){

    }
    public static void initializeClient(){
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlock.AIR_ICE, RenderLayer.getTranslucent());
    }

    public static Block register(Block block, String id,boolean shouldRegisterItem){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, itemID, blockItem);
        }
        return Registry.register(Registries.BLOCK, itemID, block);
    }
}
