package com.soybeani.items.item;

import com.soybeani.entity.custom.BulletEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.minecraft.world.tick.Tick;
import org.jetbrains.annotations.Nullable;

/**
 * @author soybean
 * @date 2024/12/28 11:42
 * @description
 */
public class BulletItem extends Item implements ProjectileItem {
    public BulletItem(Item.Settings settings) {
        super(settings);
    }
    public BulletEntity.Type type;

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return new BulletEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), (ItemStack)null,type);
//        bulletEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
    }

    public PersistentProjectileEntity createBullet(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new BulletEntity(world, shooter, stack.copyWithCount(1), shotFrom, type);
    }

    public void setType(BulletEntity.Type type){
        this.type = type;
    }
    public boolean hasType(){
        return type != null;
    }
}
