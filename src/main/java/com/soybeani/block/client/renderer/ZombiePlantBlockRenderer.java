package com.soybeani.block.client.renderer;


import com.soybeani.block.custom.PigPlantBlock;
import com.soybeani.block.custom.ZombiePlantBlock;
import com.soybeani.block.entity.PigPlantBlockEntity;
import com.soybeani.block.entity.ZombiePlantBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PigEntity;

public class ZombiePlantBlockRenderer implements BlockEntityRenderer<ZombiePlantBlockEntity> {
    private ZombieEntity zombieEntity;

    public ZombiePlantBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        // 空构造函数，不在这里创建实体
    }

    private void ensureCowEntity() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (zombieEntity == null && client.world != null) {
            zombieEntity = new ZombieEntity(EntityType.ZOMBIE, client.world);
            zombieEntity.setNoGravity(true);
            zombieEntity.setInvulnerable(true);
            // 禁用AI和移动
            zombieEntity.setAiDisabled(true);
            zombieEntity.setVelocity(0, 0, 0);
            // 固定pitch和yaw
            zombieEntity.setPitch(0);
            zombieEntity.setYaw(0);
            zombieEntity.prevYaw = 0;
            zombieEntity.prevPitch = 0;
            // 禁用头部转动
            zombieEntity.setHeadYaw(0);
            zombieEntity.prevHeadYaw = 0;
            zombieEntity.bodyYaw = 0;
            zombieEntity.prevBodyYaw = 0;
        }
    }

    @Override
    public void render(ZombiePlantBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ensureCowEntity();
        if (zombieEntity == null) return;
        // 在每次渲染时重置姿态，防止动画
        setMovement(zombieEntity);
        matrices.push();
        BlockState state = entity.getCachedState();
        if (state.getBlock() instanceof ZombiePlantBlock zombiePlantBlock) {
            float scale = zombiePlantBlock.getScale(state);
            matrices.translate(0.5, 0.0, 0.5);
            matrices.scale(scale, scale, scale);
            matrices.translate(0, 0.01, 0);

            MinecraftClient.getInstance().getEntityRenderDispatcher().render(
                    zombieEntity, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, vertexConsumers, light
            );
        }
        matrices.pop();
    }

    public void setMovement(ZombieEntity zombieEntity){
        zombieEntity.setHeadYaw(0);
        zombieEntity.prevHeadYaw = 0;
        zombieEntity.bodyYaw = 0;
        zombieEntity.prevBodyYaw = 0;
        zombieEntity.setPitch(0);
        zombieEntity.setYaw(0);
        zombieEntity.prevYaw = 0;
        zombieEntity.prevPitch = 0;
        zombieEntity.age = 0; // 防止年龄动画
        zombieEntity.limbAnimator.setSpeed(0); // 停止肢体动画
        zombieEntity.handSwinging = false; // 停止手臂摆动
    }
}
