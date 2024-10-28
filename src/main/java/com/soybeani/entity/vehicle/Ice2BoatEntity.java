package com.soybeani.entity.vehicle;

import com.soybeani.block.ModBlock;
import com.soybeani.config.InitValue;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
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

    private boolean fly = false;
    private boolean freeze = false;
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
            BlockState frostIceState = Blocks.FROSTED_ICE.getDefaultState();
            BlockState airIceState = ModBlock.AIR_ICE.getDefaultState();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Iterator var7 = BlockPos.iterate(blockPos.add(-range, -1, -range), blockPos.add(range, -1, range)).iterator();
            while (var7.hasNext()) {
                BlockPos blockPos2 = (BlockPos) var7.next();
                if (blockPos2.isWithinDistance(this.getPos(), (double) range)) {
                    mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                    BlockState blockState2 = world.getBlockState(mutable);
                    if (blockState2.isAir()) {
                        BlockState blockState3 = world.getBlockState(blockPos2);
                        if ((blockState3 == FrostedIceBlock.getMeltedState() || blockState3 == frostIceState) && frostIceState.canPlaceAt(world, blockPos2) && world.canPlace(frostIceState, blockPos2, ShapeContext.absent())) {
                            world.setBlockState(blockPos2, frostIceState);
                            world.scheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, 200);
                        }
                        if (getFreeze()) {
                            if (blockState3 == Blocks.AIR.getDefaultState() && airIceState.canPlaceAt(world, blockPos2)) {
                                world.setBlockState(blockPos2, airIceState);
                                world.scheduleBlockTick(blockPos2, ModBlock.AIR_ICE, 200);
                            }
                        }
                    }
                }
            }
        }
        if (getFly()) {
            Vec3d velocity = this.getVelocity();
            if(velocity.getY() < 1){
                this.setVelocity(velocity.x, velocity.y + 0.1, velocity.z);
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
        if (!this.getWorld().isClient()) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // 获取当前客户端的玩家
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity clientPlayer = client.player;

        // 获取船的驾驶员
        Entity driver = this.getControllingPassenger();

        // 如果没有驾驶员，或者当前客户端玩家不是驾驶员，播放静止动画
        if (driver == null || clientPlayer == null) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // 只有当客户端玩家是驾驶员时才检查按键状态
        boolean pressingForward = client.options.forwardKey.isPressed();
        boolean pressingLeft = client.options.leftKey.isPressed();
        boolean pressingRight = client.options.rightKey.isPressed();

        if (getFly()) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.fly", Animation.LoopType.LOOP));
        } else if (pressingForward) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.move", Animation.LoopType.LOOP));
        } else if (pressingLeft) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.turnleft", Animation.LoopType.LOOP));
        } else if (pressingRight) {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.turnright", Animation.LoopType.LOOP));
        } else {
            animationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.idle", Animation.LoopType.LOOP));
        }

        return PlayState.CONTINUE;
    }

    public void setFly(boolean b) {
        this.fly = b;
    }

    public boolean getFly() {
        return this.fly;
    }

    public void setFreeze(boolean b) {
        this.freeze = b;
    }

    public boolean getFreeze() {
        return this.freeze;
    }
}

