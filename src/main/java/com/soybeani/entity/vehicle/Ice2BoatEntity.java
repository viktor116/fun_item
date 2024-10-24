package com.soybeani.entity.vehicle;

import com.soybeani.block.ModBlock;
import com.soybeani.config.InitValue;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.Iterator;

/**
 * @author soybean
 * @date 2024/10/23 12:15
 * @description
 */
// CustomBoatEntity.java
public class Ice2BoatEntity extends BoatEntity implements GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public static final EntityType<Ice2BoatEntity> ICE2_BOAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "ice2_boat"),
            FabricEntityTypeBuilder.<Ice2BoatEntity>create(SpawnGroup.MISC, Ice2BoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public Ice2BoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public Ice2BoatEntity(World world, double x, double y, double z) {
        this(ICE2_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    public void tick() {
        if (!(isSubmergedInWater() || isTouchingWater())) {
            int range = 8;
            BlockPos blockPos = this.getBlockPos();
            World world = this.getWorld();
            BlockState blockState = ModBlock.AIR_ICE.getDefaultState();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Iterator var7 = BlockPos.iterate(blockPos.add(-range, -1, -range), blockPos.add(range, -1, range)).iterator();
            while (var7.hasNext()) {
                BlockPos blockPos2 = (BlockPos) var7.next();
                if (blockPos2.isWithinDistance(this.getPos(), (double) range)) {
                    mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                    BlockState blockState2 = world.getBlockState(mutable);
                    if (blockState2.isAir()) {
                        BlockState blockState3 = world.getBlockState(blockPos2);
                        if ((blockState3 == FrostedIceBlock.getMeltedState() || blockState3 == blockState) && blockState.canPlaceAt(world, blockPos2) && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
                            world.setBlockState(blockPos2, blockState);
                            world.scheduleBlockTick(blockPos2, ModBlock.AIR_ICE, 200);
                        }
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    public Item asItem() {
        return ItemsRegister.ICE_BOAT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private PlayState predicate(AnimationState<GeoAnimatable> animationState) {
        Vec3d velocity = this.getVelocity();
        // 计算水平速度（x和z方向）
        double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        // 设置一个速度阈值，用于判断是否在"移动"
        final double MOVEMENT_THRESHOLD = 0.01;
        if (horizontalSpeed > MOVEMENT_THRESHOLD) {
            // 当速度超过阈值时播放移动动画
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.move", Animation.LoopType.LOOP));
        }
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
}

