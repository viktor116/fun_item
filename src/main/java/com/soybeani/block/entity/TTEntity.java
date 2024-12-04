package com.soybeani.block.entity;

import com.soybeani.block.ModBlock;
import com.soybeani.block.custom.TTBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @author soybean
 * @date 2024/11/30 16:01
 * @description
 */
public class TTEntity extends Entity implements Ownable{  // 改为直接继承 Entity

    private static final TrackedData<Integer> FUSE = DataTracker.registerData(TTEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<BlockState> BLOCK_STATE = DataTracker.registerData(TTEntity.class, TrackedDataHandlerRegistry.BLOCK_STATE);
    private static final int DEFAULT_FUSE = 80;
    private static final String BLOCK_STATE_NBT_KEY = "block_state";
    public static final String FUSE_NBT_KEY = "fuse";
    private static final float EXPLOSION_RADIUS = 4.0f;
    private static final int CHAIN_FUSE = 20; // 连锁反应的引信时间 (1秒)
    private static final ExplosionBehavior TELEPORTED_EXPLOSION_BEHAVIOR = new ExplosionBehavior(){
        @Override
        public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
            if (state.isOf(Blocks.NETHER_PORTAL)) {
                return false;
            }
            return super.canDestroyBlock(explosion, world, pos, state, power);
        }

        @Override
        public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
            if (blockState.isOf(Blocks.NETHER_PORTAL)) {
                return Optional.empty();
            }
            return super.getBlastResistance(explosion, world, pos, blockState, fluidState);
        }
    };

    public TTEntity(EntityType<? extends TTEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    public TTEntity(World world, double x, double y, double z, LivingEntity igniter) {
        this((EntityType<? extends TTEntity>)ModBlock.TT_ENTITY, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.setFuse(80);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    public TTEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter, boolean chain) {
        this((EntityType<TTEntity>)ModBlock.TT_ENTITY, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465D;
        this.setVelocity(-Math.sin(d) * 0.02D, 0.20000000298023224D, -Math.cos(d) * 0.02D);
        this.setFuse(chain ? CHAIN_FUSE : DEFAULT_FUSE); // 根据是否是连锁反应设置不同的引信时间
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(FUSE, 80);
        builder.add(BLOCK_STATE, Blocks.TNT.getDefaultState());
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public void setFuse(int fuse) {
        this.dataTracker.set(FUSE, fuse);
    }
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putShort("Fuse", (short)this.getFuse());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.setFuse(nbt.getShort("Fuse"));
    }

    @Override
    public void tick() {
        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98D));

        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().multiply(0.7D, -0.5D, 0.7D));
        }

        int currentFuse = this.getFuse() - 1;
        this.setFuse(currentFuse);

        if (currentFuse <= 0) {
            this.discard();
            if (!this.getWorld().isClient) {
                this.explode();
            }
        } else {
            this.getWorld().addParticle(
                    ParticleTypes.SMOKE,
                    this.getX(), this.getY() + 0.5, this.getZ(),
                    0.0, 0.0, 0.0
            );
        }
    }

    private void explode() {
        World world = this.getWorld();
        if (!world.isClient) {
            // 获取爆炸中心点
            Vec3d explosionPos = this.getPos();

            // 获取爆炸范围内的所有方块
            Box explosionBox = new Box(
                    explosionPos.x - EXPLOSION_RADIUS, explosionPos.y - EXPLOSION_RADIUS, explosionPos.z - EXPLOSION_RADIUS,
                    explosionPos.x + EXPLOSION_RADIUS, explosionPos.y + EXPLOSION_RADIUS, explosionPos.z + EXPLOSION_RADIUS
            );

            // 先检查并触发其他TT方块
            BlockPos.iterate(
                    new BlockPos((int)(explosionBox.minX), (int)(explosionBox.minY), (int)(explosionBox.minZ)),
                    new BlockPos((int)(explosionBox.maxX), (int)(explosionBox.maxY), (int)(explosionBox.maxZ))
            ).forEach(pos -> {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof TTBlock) {
                    // 触发其他TT方块
                    TTBlock.primeTnt(world, pos, true);
                    world.removeBlock(pos, false);
                }
            });

            // 原有的爆炸效果逻辑
            BlockPos.iterate(
                    new BlockPos((int)(explosionBox.minX), (int)(explosionBox.minY), (int)(explosionBox.minZ)),
                    new BlockPos((int)(explosionBox.maxX), (int)(explosionBox.maxY), (int)(explosionBox.maxZ))
            ).forEach(pos -> {
                BlockState state = world.getBlockState(pos);
                if (!state.isAir() && !state.isLiquid()) {
                    // 计算到爆炸中心的方向和距离
                    Vec3d blockCenter = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    Vec3d direction = blockCenter.subtract(explosionPos).normalize();
                    double distance = blockCenter.distanceTo(explosionPos);

                    // 根据距离计算力度
                    double power = Math.max(0.5, (EXPLOSION_RADIUS - distance) / EXPLOSION_RADIUS * 2.0);

                    // 创建并发射方块实体
                    FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world, pos, state);
                    if (fallingBlock != null) {
                        fallingBlock.setPosition(blockCenter.x, blockCenter.y, blockCenter.z);

                        // 添加随机性
                        double randomX = (world.random.nextDouble() - 0.5) * 0.2;
                        double randomY = (world.random.nextDouble() - 0.5) * 0.2;
                        double randomZ = (world.random.nextDouble() - 0.5) * 0.2;

                        // 设置速度
                        fallingBlock.setVelocity(
                                direction.x * power + randomX,
                                direction.y * power + 0.8 + randomY,
                                direction.z * power + randomZ
                        );
                        fallingBlock.velocityModified = true;

                        // 移除原方块
                        world.removeBlock(pos, false);
                    }
                }
            });

            // 处理范围内的实体
            List<Entity> entities = world.getOtherEntities(this, explosionBox);
            for (Entity entity : entities) {
                // 计算到爆炸中心的方向和距离
                Vec3d entityPos = entity.getPos();
                Vec3d direction = entityPos.subtract(explosionPos).normalize();
                double distance = entityPos.distanceTo(explosionPos);

                // 根据距离计算击退力度
                double power = Math.max(0.5, (EXPLOSION_RADIUS - distance) / EXPLOSION_RADIUS * 2.0);

                // 为TTEntity增加额外的力度
                if (entity instanceof TTEntity) {
                    power *= 1.5; // 对其他TT实体的击退力度增加50%
                }

                // 添加随机性
                double randomX = (world.random.nextDouble() - 0.5) * 0.2;
                double randomY = (world.random.nextDouble() - 0.5) * 0.2;
                double randomZ = (world.random.nextDouble() - 0.5) * 0.2;

                // 设置实体速度
                Vec3d velocity = new Vec3d(
                        direction.x * power + randomX,
                        direction.y * power + 0.8 + randomY, // 增加向上的力度
                        direction.z * power + randomZ
                );

                entity.setVelocity(velocity);
                entity.velocityModified = true;

                // 如果是TTEntity，重置它的引信时间（可选）
                if (entity instanceof TTEntity ttEntity && distance <= EXPLOSION_RADIUS * 0.5) {
                    ttEntity.setFuse(CHAIN_FUSE); // 近距离爆炸会触发快速引信
                }
            }
            // 播放爆炸音效和粒子效果
            world.playSound(null, explosionPos.x, explosionPos.y, explosionPos.z,
                    SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.0F);

            for (int i = 0; i < 50; i++) {
                double px = explosionPos.x + (world.random.nextDouble() - 0.5) * EXPLOSION_RADIUS * 2;
                double py = explosionPos.y + (world.random.nextDouble() - 0.5) * EXPLOSION_RADIUS * 2;
                double pz = explosionPos.z + (world.random.nextDouble() - 0.5) * EXPLOSION_RADIUS * 2;
                world.addParticle(ParticleTypes.EXPLOSION, px, py, pz, 0.0, 0.0, 0.0);
            }
        }

        this.discard();
    }

    public BlockState getBlockState() {
        return ModBlock.TT_BLOCK.getDefaultState();
    }
    private LivingEntity causingEntity;
    @Nullable
    @Override
    public Entity getOwner() {
        return this.causingEntity;
    }
}