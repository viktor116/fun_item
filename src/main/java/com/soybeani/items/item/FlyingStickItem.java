package com.soybeani.items.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/11/30 11:35
 * @description
 */
public class FlyingStickItem extends Item {

    public FlyingStickItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context){
        World world = context.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        // 检查方块是否可以被破坏
        if (!state.isAir() && !state.isLiquid()) {
            // 创建掉落方块实体
            FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world, pos, state);

            if (fallingBlock != null) {
                // 计算发射方向和力度
                double power = 1.5; // 调整这个数值来改变发射力度
                Vec3d direction = context.getPlayer().getRotationVector();

                // 设置方块的速度
                fallingBlock.setVelocity(
                        direction.x * power,
                        direction.y * power + 0.5, // 添加一些向上的力
                        direction.z * power
                );
                fallingBlock.setNoGravity(false); // 保留重力效果

                // 移除原始方块
                world.removeBlock(pos, false);

                // 播放声音效果（可选）
                // world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            // 对物品造成耐久损耗（可选）
            // context.getStack().damage(1, context.getPlayer(), (player) -> {
            //     player.sendToolBreakStatus(context.getHand());
            // });

            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }
}
