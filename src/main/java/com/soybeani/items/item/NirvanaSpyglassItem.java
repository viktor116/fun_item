package com.soybeani.items.item;

import com.soybeani.entity.custom.PurpleLightningEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SpyglassItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * @author soybean
 * @date 2024/11/6 16:38
 * @description
 */
public class NirvanaSpyglassItem extends SpyglassItem {
    private boolean openLightning = false;
    private static final int GENERAL_MODE = 1;
    private static final int LINEAR_MODE = 2;
    private static final int STRAIGHT_MODE = 3;
    private static final int CROSS_MODE = 4;
    private static final int RECTANGLE_MODE = 5;
    private static final int CIRCLE_MODE = 6;
    private int spyglassMode = 1;
    public static final int LEVEL_MAX = 6;
    public NirvanaSpyglassItem(Settings settings) {
        super(settings);
        openLightning = false;
    }

    public void lookLightning(PlayerEntity player, int mode){
        if(!openLightning) return;
        BlockHitResult hitResult = (BlockHitResult) player.raycast(300.0D, 0.0F, true);
        BlockPos blockPos = hitResult.getBlockPos();
        World world = player.getWorld();
        PurpleLightningEntity lightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
        int spacing = 2;    // 闪电之间的间距
        Vec3d lookDir = player.getRotationVector();
        // 获取垂直于视角方向的向量（用于横向扩展）
        Vec3d perpendicular = lookDir.crossProduct(new Vec3d(0, 1, 0)).normalize();
        Vec3d direction = player.getRotationVector().normalize();
        Vec3d playerPos = player.getPos();
        int lightningCount = 5;  // 每边生成的闪电数量
        switch (mode){
            case GENERAL_MODE:
                lightning.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                world.spawnEntity(lightning);
                break;
            case LINEAR_MODE:
                lightning.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                world.spawnEntity(lightning);
                // 向两边扩展生成闪电
                for (int i = 1; i <= lightningCount; i++) {
                    // 右侧闪电
                    BlockPos rightPos = getOffsetPosition(blockPos, perpendicular, spacing * i);
                    PurpleLightningEntity rightLightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                    rightLightning.setPosition(rightPos.getX(), rightPos.getY(), rightPos.getZ());
                    world.spawnEntity(rightLightning);

                    // 左侧闪电
                    BlockPos leftPos = getOffsetPosition(blockPos, perpendicular, -spacing * i);
                    PurpleLightningEntity leftLightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                    leftLightning.setPosition(leftPos.getX(), leftPos.getY(), leftPos.getZ());
                    world.spawnEntity(leftLightning);
                }
                break;
            case STRAIGHT_MODE:
                int count = (int) Math.sqrt(Math.pow(playerPos.x - blockPos.getX(), 2) + Math.pow(playerPos.z - blockPos.getZ(), 2));
                double startDistance = 5.0;  // 第一个闪电距离玩家的距离

                // 沿着视线方向生成一系列闪电
                for (int i = 0; i < count; i++) {
                    // 计算当前闪电应该生成的距离
                    double distance = startDistance + (i * spacing);
                    // 计算闪电生成的位置
                    Vec3d lightningPos = playerPos.add(
                            direction.x * distance,
                            direction.y * distance,
                            direction.z * distance
                    );

                    // 在计算出的位置生成闪电
                    lightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                    lightning.setPosition(lightningPos.getX(), lightningPos.getY(), lightningPos.getZ());
                    world.spawnEntity(lightning);
                }
                break;
            case CROSS_MODE:
                lightning.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                world.spawnEntity(lightning);
                // 向两边扩展生成闪电
                for (int i = 1; i <= lightningCount*2; i++) {
                    // 前方
                    BlockPos frontPos = getOffsetPosition(blockPos, player.getRotationVector().normalize(), spacing * i);
                    PurpleLightningEntity frontLightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                    frontLightning.setPosition(frontPos.getX(), frontPos.getY(), frontPos.getZ());
                    world.spawnEntity(frontLightning);

                    // 后方
                    BlockPos backPos = getOffsetPosition(blockPos, player.getRotationVector().normalize(), -spacing * i);
                    PurpleLightningEntity backLightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                    backLightning.setPosition(backPos.getX(), backPos.getY(), backPos.getZ());
                    world.spawnEntity(backLightning);

                    // 右侧闪电
                    BlockPos rightPos = getOffsetPosition(blockPos, perpendicular, spacing * i);
                    PurpleLightningEntity rightLightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                    rightLightning.setPosition(rightPos.getX(), rightPos.getY(), rightPos.getZ());
                    world.spawnEntity(rightLightning);

                    // 左侧闪电
                    BlockPos leftPos = getOffsetPosition(blockPos, perpendicular, -spacing * i);
                    PurpleLightningEntity leftLightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                    leftLightning.setPosition(leftPos.getX(), leftPos.getY(), leftPos.getZ());
                    world.spawnEntity(leftLightning);
                }
                break;
            case RECTANGLE_MODE:
                double sideLength = 20.0;
                // 生成正方形区域的四个角的位置
                Vec3d topLeft = playerPos.add(-sideLength/2, 0, -sideLength/2);
                Vec3d topRight = playerPos.add(sideLength/2, 0, -sideLength/2);
                Vec3d bottomLeft = playerPos.add(-sideLength/2, 0, sideLength/2);
                Vec3d bottomRight = playerPos.add(sideLength/2, 0, sideLength/2);
                // 沿正方形边缘生成闪电
                generateLightningOnLine(topLeft, topRight, world);
                generateLightningOnLine(topRight, bottomRight, world);
                generateLightningOnLine(bottomRight, bottomLeft, world);
                generateLightningOnLine(bottomLeft, topLeft, world);
                break;
            case CIRCLE_MODE:

                double radius = Math.sqrt(Math.pow(blockPos.getX() - playerPos.x, 2)  + Math.pow(blockPos.getZ() - playerPos.z, 2));

                // 沿圆形生成闪电
                double circumference = 2 * Math.PI * radius;
                int lightningNum = (int) Math.ceil(circumference / 2.0);

                for (int i = 0; i < lightningNum; i++) {
                    double angle = i * 2 * Math.PI / lightningNum;
                    double x = playerPos.x + radius * Math.cos(angle);
                    double z = playerPos.z + radius * Math.sin(angle);

                    lightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
                    lightning.setPosition(x, playerPos.y, z);
                    world.spawnEntity(lightning);
                }
                break;
        }
    }

    public static String getModeName(int num){
        switch (num) {
            case GENERAL_MODE -> {
                return "普通模式";
            }
            case LINEAR_MODE -> {
                return "排行模式";
            }
            case STRAIGHT_MODE -> {
                return "路径模式";
            }
            case CROSS_MODE -> {
                return "十字模式";
            }
            case RECTANGLE_MODE -> {
                return "矩形模式";
            }
            case CIRCLE_MODE -> {
                return "圆形模式";
            }
            default -> {
                return "Unknown Mode";
            }
        }
    }
    private static BlockPos getOffsetPosition(BlockPos original, Vec3d direction, double distance) {
        return new BlockPos(
                (int) (original.getX() + Math.round(direction.x * distance)),
                original.getY(),
                (int)(original.getZ() + Math.round(direction.z * distance))
        );
    }

    private void generateLightningOnLine(Vec3d start, Vec3d end, World world) {
        double distance = start.distanceTo(end);
        int lightningCount = (int) Math.ceil(distance / 2.0);

        for (int i = 0; i < lightningCount; i++) {
            double t = i / (double) lightningCount;
            Vec3d pos = start.lerp(end, t);

            PurpleLightningEntity lightning = new PurpleLightningEntity(PurpleLightningEntity.PURPLE_LIGHTNING, world);
            lightning.setPosition(pos.x, pos.y, pos.z);
            world.spawnEntity(lightning);
        }
    }

    public void setSpyglassMode(int spyglassMode){
        this.spyglassMode = spyglassMode;
    }

    public int getSpyglassMode(){
        return this.spyglassMode;
    }

    public void setOpenLightning(boolean b){
        this.openLightning = b;
    }

    public boolean getOpenLightning(){
        return this.openLightning;
    }
}
