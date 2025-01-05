package com.soybeani.entity.custom;

import com.soybeani.entity.EntityRegister;
import com.soybeani.items.ItemsRegister;
import com.soybeani.items.item.GatlingItem;
import com.soybeani.items.item.TalismanItem;
import com.soybeani.utils.CommonUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author soybean
 * @date 2024/12/28 11:44
 * @description
 */
public class BulletEntity extends PersistentProjectileEntity {

    private static final TrackedData<Integer> BULLET_TYPE = DataTracker.registerData(BulletEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final int MAX_EXIST_TIME = 200;

    private int already_exist_time = 0;

    public Type type = Type.COPPER;

    private double damage;

    public BulletEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    public BulletEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world,Type type) {
        super(entityType, world);
        this.setType(type);
    }

    public BulletEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityRegister.BULLET_ENTITY_TYPE, x, y, z, world, stack, shotFrom);
    }
    public BulletEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom,Type type) {
        super(EntityRegister.BULLET_ENTITY_TYPE, x, y, z, world, stack, shotFrom);
        this.setType(type);
    }

    public BulletEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityRegister.BULLET_ENTITY_TYPE, owner, world, stack, shotFrom);
    }

    public BulletEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom,Type type) {
        super(EntityRegister.BULLET_ENTITY_TYPE, owner, world, stack, shotFrom);
        this.setType(type);
    }

    {
        setDefaultDamage();
    }
    @Override
    protected ItemStack getDefaultItemStack() {
        return ItemsRegister.COPPER_BULLET.getDefaultStack();
    }
    @Override
    public void tick() {
        super.tick();
        if(!this.inGround){
            if(!this.getWorld().isClient){
                ((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.SMOKE,
                        this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(),
                        2,
                        0.2, 0.2, 0.2,
                        0.1
                );
            }
        }
        if(already_exist_time < MAX_EXIST_TIME){
            already_exist_time++;
        }else {
            this.discard();
            already_exist_time = 0;
        }
    }
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BULLET_TYPE, Type.COPPER.ordinal());
    }

    public void setType(Type type) {
        this.type = type;
        this.dataTracker.set(BULLET_TYPE,type.ordinal());
    }

    public Type getBulletType() {
        return Type.values()[this.dataTracker.get(BULLET_TYPE)];
    }
    public boolean hasType(){
        return type != null;
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity owner = this.getOwner();
        if(owner ==null) return;
        Entity target = entityHitResult.getEntity();
        if(owner instanceof PlayerEntity player){
            if(player.getMainHandStack().getItem() instanceof GatlingItem && player.getOffHandStack().getItem() instanceof TalismanItem talismanItem){
                if(talismanItem.getType() == TalismanItem.Type.BLUE) TalismanItem.handleLightningSpell(player, target.getBlockPos(),player.getOffHandStack());
                if(talismanItem.getType() == TalismanItem.Type.YELLOW_RED) TalismanItem.handleFlameToEntity(player, target, player.getOffHandStack(), 100);
                if(talismanItem.getType() == TalismanItem.Type.GREEN) TalismanItem.handleNatureHealing(player,player.getOffHandStack(),(float) this.getDamage());
                if(talismanItem.getType() == TalismanItem.Type.BLACK_PURPLE) TalismanItem.handleWitherStatus(player,target,target.getWorld(),player.getOffHandStack());
                if(talismanItem.getType() == TalismanItem.Type.SKYBLUE) TalismanItem.handleFlySkyToEntity(player,target,player.getOffHandStack());
            }
        }
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
//        super.onBlockHit(blockHitResult);
        this.discard();
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public void setDamage(double damage) {
        this.damage = damage;
        super.setDamage(damage);
    }

    public void setDefaultDamage() {
        switch(type){
            case COPPER -> damage = 2.0;
            case IRON -> damage = 4.0;
            default -> damage = 1.0;
        }
        super.setDamage(damage);
    }

    public enum Type{
        COPPER,
        IRON
    }


}
