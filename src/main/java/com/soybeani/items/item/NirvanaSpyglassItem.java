package com.soybeani.items.item;

import com.soybeani.entity.custom.PurpleLightningEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SpyglassItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * @author soybean
 * @date 2024/11/6 16:38
 * @description
 */
public class NirvanaSpyglassItem extends SpyglassItem {
    private boolean openLightning = false;
    private int level = 1;
    public static final int LEVEL_MAX = 15;
    public NirvanaSpyglassItem(Settings settings) {
        super(settings);
        openLightning = false;
    }

    public void lookLightning(PlayerEntity player, int level){
        if(!openLightning) return;
        BlockHitResult hitResult = (BlockHitResult) player.raycast(300.0D, 0.0F, true);
        BlockPos blockPos = hitResult.getBlockPos();
        World world = player.getWorld();
        int i = level - 1;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Iterator var7;
        var7 = BlockPos.iterate(blockPos.add(-i, 0, -i), blockPos.add(i, 0, i)).iterator();
        while (var7.hasNext()) {
            BlockPos blockPos2 = (BlockPos) var7.next();
            mutable.set(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
            BlockState blockState3 = world.getBlockState(blockPos2);
            if (blockState3 != Blocks.AIR.getDefaultState() && world.getBlockState(blockPos2.up()) == Blocks.AIR.getDefaultState()) {
                PurpleLightningEntity lightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                lightning.setPosition(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
                world.spawnEntity(lightning);
            }
        }
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return this.level;
    }

    public void setOpenLightning(boolean b){
        this.openLightning = b;
    }

    public boolean getOpenLightning(){
        return this.openLightning;
    }
}
