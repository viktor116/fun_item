package com.soybeani.items.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Arrays;

public class DetectStaffItem extends Item {
    private final boolean isGoldenStaff;

    public DetectStaffItem(Settings settings, boolean isGoldenStaff) {
        super(settings);
        this.isGoldenStaff = isGoldenStaff;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos clickedPos = context.getBlockPos();
        Direction clickedFace = context.getSide();

        if (world.isClient || player == null) return ActionResult.PASS;

        // 探测距离
        int maxDetectionDepth = 300;

        // 根据点击的面确定探测方向
        BlockPos.Mutable currentPos = clickedPos.mutableCopy();
        // 方向修正：点击顶部向下，点击底部向上
        Direction detectionDirection = clickedFace.getOpposite();

        int oresFound = 0;
        for (int i = 1; i <= maxDetectionDepth; i++) {
            currentPos.move(detectionDirection);
            BlockState blockState = world.getBlockState(currentPos);

            // 判断是否是要探测的矿石
            if (isOreToDetect(blockState)) {
                oresFound++;
                // 发送探测到矿石的消息
                sendOreDetectionMessage(player, currentPos, blockState, i, detectionDirection);
            }
        }
        // 如果没有找到矿石
        if (oresFound == 0) {
            player.sendMessage(Text.literal("未发现矿石"), true);
        }

        return ActionResult.SUCCESS;
    }


    private String getDirectionName(Direction direction) {
        switch(direction) {
            case UP: return "上方";
            case DOWN: return "下方";
            case NORTH: return "北方";
            case SOUTH: return "南方";
            case EAST: return "东方";
            case WEST: return "西方";
            default: return "未知";
        }
    }
    private boolean isOreToDetect(BlockState blockState) {
        if (isGoldenStaff) {
            return isSpecificOre(blockState);
        }

        return isSomeOre(blockState);
    }

    private boolean isSomeOre(BlockState blockState) {
        Block[] oreBlocks = {
                Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
                Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
                Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
                Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
        };
        return Arrays.stream(oreBlocks).anyMatch(blockState::isOf);
    }

    private boolean isSpecificOre(BlockState blockState) {
        // 定义所有可探测的矿石类型
        Block[] oreBlocks = {
                Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
                Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
                Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
                Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.NETHER_GOLD_ORE,
                Blocks.NETHER_QUARTZ_ORE,
                Blocks.ANCIENT_DEBRIS
        };

        return Arrays.stream(oreBlocks).anyMatch(blockState::isOf);
    }

    private void sendOreDetectionMessage(PlayerEntity player, BlockPos pos, BlockState blockState, int distance, Direction direction) {
        // 根据矿石类型生成不同的消息
        String oreName = getOreName(blockState);
        Text message = Text.literal(String.format(
                "在%s %d 格处探测到 %s",
                getDirectionName(direction),
                distance,
                oreName
        ));
        player.getWorld().playSound(null,player.getPos().getX(),player.getPos().getY(),player.getPos().getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS,1f,1f);
        player.sendMessage(message, true);
    }

    private String getOreName(BlockState blockState) {
        // 返回矿石的本地化名称
        return blockState.getBlock().getName().getString();
    }
}
