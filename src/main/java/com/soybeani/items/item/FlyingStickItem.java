package com.soybeani.items.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
        if (!state.isAir() && !state.isLiquid()) {
            // 创建掉落方块实体
            FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world, pos, state);

            if (fallingBlock != null) {
                // 增加初始位置偏移，防止卡在原方块位置
                fallingBlock.setPosition(
                        pos.getX() + 0.5,
                        pos.getY() + 0.1,
                        pos.getZ() + 0.5
                );

                // 调整发射力度和方向
                double power = 1.0; // 降低基础力度使运动更明显
                Vec3d direction = context.getPlayer().getRotationVector();

                // 添加一些随机性到速度中
                double randomX = (world.random.nextDouble() - 0.5) * 0.2;
                double randomY = (world.random.nextDouble() - 0.5) * 0.2;
                double randomZ = (world.random.nextDouble() - 0.5) * 0.2;

                double directionY = direction.getY();
                if(direction.getY() < 0){
                    BlockState blockDown = world.getBlockState(pos.down());
                    if(blockDown != Blocks.AIR.getDefaultState() && blockDown.isSolidBlock(world, pos.down())){
                        directionY = -directionY;
                    }
                }
                Vec3d playerVec = context.getPlayer().getVelocity();
                power = power + Math.sqrt(Math.pow(playerVec.getZ(),2)+Math.pow(playerVec.getX(),2)+Math.pow(playerVec.getY(),2));
                // 设置方块的速度
                fallingBlock.setVelocity(
                        direction.x * power + randomX,
                        directionY * power + 0.8 + randomY, // 增加向上的力
                        direction.z * power + randomZ
                );

                // 保持重力但减小影响
                fallingBlock.setNoGravity(false);
                fallingBlock.velocityModified = true; // 确保速度更新

                // 添加粒子效果
                for (int i = 0; i < 10; i++) {
                    double px = pos.getX() + world.random.nextDouble();
                    double py = pos.getY() + world.random.nextDouble();
                    double pz = pos.getZ() + world.random.nextDouble();
                    world.addParticle(
                            ParticleTypes.EXPLOSION,
                            px, py, pz,
                            0.0, 0.0, 0.0
                    );
                }

                // 移除原始方块
                world.removeBlock(pos, false);

                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_BIG_FALL,SoundCategory.BLOCKS, 1.0F, 1.0F + (world.random.nextFloat() - 0.5F) * 0.2F );
            }
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }
}
