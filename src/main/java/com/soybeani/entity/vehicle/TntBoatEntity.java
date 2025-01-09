package com.soybeani.entity.vehicle;

import com.soybeani.block.ModBlock;
import com.soybeani.block.entity.TTEntity;
import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.items.ItemsRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * @author soybean
 * @date 2024/10/23 12:15
 * @description
 */
// CustomBoatEntity.java
public class TntBoatEntity extends ChestBoatEntity {

    private boolean isAutoMode = false;
    private boolean isRandomMode = false;
    private int autoModeTimer = 0;
    private static final int AUTO_MAX_TIME = 20;

    public TntBoatEntity(EntityType<? extends ChestBoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public TntBoatEntity(World world, double x, double y, double z) {
        this(EntityRegister.TNT_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }
    @Override
    public void tick() {
        if(this.getIsAutoMode() && this.hasPassengers()){
            if(this.getPassengerList().get(0) instanceof LivingEntity entity){
                if(hasTntInInventory()){
                    this.autoModeTimer++;
                    if (this.autoModeTimer >= AUTO_MAX_TIME) {
                        this.autoModeTimer = 0;
                        double bodyYawInRadians = Math.toRadians(entity.getBodyYaw());
                        this.launchTntAtTarget(entity, entity.getPos().add(-Math.sin(bodyYawInRadians),0, Math.cos(bodyYawInRadians)),this.getIsRandomMode());
                    }
                }else {
                    this.setAutoMode(false);
                    this.autoModeTimer = 0;
                }
            }
        }
        super.tick();
    }

    @Override
    public Item asItem() {
        return ItemsRegister.TNT_BOAT;
    }

    // 自定义检查容器中是否包含 TNT 物品的函数
    public boolean hasTntInInventory() {
        // 获取船的物品容器
        DefaultedList<ItemStack> inventory = this.getInventory();

        // 遍历所有槽，检查是否包含 TNT
        for (ItemStack stack: inventory) {
            if ((stack.getItem() == Items.TNT || stack.getItem() == ModBlock.TT_BLOCK.asItem())&& stack.getCount() > 0) {
                return true; // 找到 TNT，返回 true
            }
        }
        // 如果没有找到 TNT，返回 false
        return false;
    }

    // 发射 TNT 方法
    public void launchTntAtTarget(LivingEntity entity, Vec3d targetPos, boolean isRandom) {
        if(entity.getWorld().isClient) return;
        if (this.hasTntInInventory()) {
            Item item= this.removeTntFromInventory();
            // 3. 获取船尾的箱子位置
            Vec3d boatTailPos = this.getPos().add(0, 0, 0);
            Vec3d direction;
            if(isRandom){
                double randomX = entity.getWorld().getRandom().nextDouble() * 2 - 1;  // [-1, 1]
                double randomY = entity.getWorld().getRandom().nextDouble() * 2 - 1;  // [-1, 1]
                double randomZ = entity.getWorld().getRandom().nextDouble() * 2 - 1;  // [-1, 1]
                Vec3d randomVec3d = new Vec3d(randomX, randomY, randomZ);
                direction = randomVec3d.normalize();  // 确保方向是单位向量
            }else{
                direction = targetPos.subtract(boatTailPos).normalize();
            }
            InitValue.LOGGER.info("direction="+direction.toString());
            double velocity = 0.5;
            Vec3d velocityVector = direction.multiply(velocity).add(0, 0.8, 0);
            if(item == Items.TNT && item != null){
                TntEntity tntEntity = new TntEntity(this.getWorld(), boatTailPos.x, boatTailPos.y, boatTailPos.z, entity);
                tntEntity.setFuse(80); // 设置 TNT 的引爆时间
                tntEntity.setVelocity(velocityVector.x, velocityVector.y, velocityVector.z);
                this.getWorld().spawnEntity(tntEntity);
            }else if(item == ModBlock.TT_BLOCK.asItem() && item != null){
                TTEntity ttEntity = new TTEntity(this.getWorld(), boatTailPos.x, boatTailPos.y, boatTailPos.z, entity);
                ttEntity.setFuse(80); // 设置 TNT 的引爆时间
                ttEntity.setVelocity(velocityVector.x, velocityVector.y, velocityVector.z);
                this.getWorld().spawnEntity(ttEntity);
            }
            // 6. 在目标位置发射 TNT
            this.getWorld().playSound(null, boatTailPos.x, boatTailPos.y, boatTailPos.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.PLAYERS, 1f, 1f);
        }else {
            if(entity instanceof PlayerEntity player) {
                player.sendMessage(Text.of("船内无TNT物品"), true);
            }
        }
    }

    // 从容器中移除 TNT 物品
    public Item removeTntFromInventory() {
        DefaultedList<ItemStack> inventory = this.getInventory();
        for (ItemStack stack: inventory) {
            if(stack.getCount() > 0){
                if(stack.getItem() == Items.TNT){
                    stack.decrement(1); // 从容器中移除 1 个 TNT
                    return Items.TNT;
                }else if (stack.getItem() == ModBlock.TT_BLOCK.asItem()) {
                    stack.decrement(1);
                    return ModBlock.TT_BLOCK.asItem();
                }
            }
        }
        return null;
    }

    public void setAutoMode(boolean autoMode) {
        this.isAutoMode = autoMode;
    }

    public boolean getIsAutoMode() {
        return this.isAutoMode;
    }

    public boolean getIsRandomMode() {
        return this.isRandomMode;
    }

    public void setIsRandomMode(boolean isRandomMode) {
        this.isRandomMode = isRandomMode;
    }

}
