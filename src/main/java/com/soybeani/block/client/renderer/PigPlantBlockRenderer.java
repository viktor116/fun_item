package com.soybeani.block.client.renderer;


import com.soybeani.block.custom.PigPlantBlock;
import com.soybeani.block.entity.PigPlantBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;

public class PigPlantBlockRenderer implements BlockEntityRenderer<PigPlantBlockEntity> {
    private PigEntity pigEntity;

    public PigPlantBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        // 空构造函数，不在这里创建实体
    }

    private void ensureCowEntity() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (pigEntity == null && client.world != null) {
            pigEntity = new PigEntity(EntityType.PIG, client.world);
            pigEntity.setNoGravity(true);
            pigEntity.setInvulnerable(true);
            // 禁用AI和移动
            pigEntity.setAiDisabled(true);
            pigEntity.setVelocity(0, 0, 0);
            // 固定pitch和yaw
            pigEntity.setPitch(0);
            pigEntity.setYaw(0);
            pigEntity.prevYaw = 0;
            pigEntity.prevPitch = 0;
            // 禁用头部转动
            pigEntity.setHeadYaw(0);
            pigEntity.prevHeadYaw = 0;
            pigEntity.bodyYaw = 0;
            pigEntity.prevBodyYaw = 0;
        }
    }

    @Override
    public void render(PigPlantBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ensureCowEntity();
        if (pigEntity == null) return;
        // 在每次渲染时重置姿态，防止动画
        setMovement(pigEntity);
        matrices.push();
        BlockState state = entity.getCachedState();
        if (state.getBlock() instanceof PigPlantBlock pigPlantBlock) {
            float scale = pigPlantBlock.getScale(state);
            matrices.translate(0.5, 0.0, 0.5);
            matrices.scale(scale, scale, scale);
            matrices.translate(0, 0.01, 0);

            MinecraftClient.getInstance().getEntityRenderDispatcher().render(
                    pigEntity, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, vertexConsumers, light
            );
        }
        matrices.pop();
    }

    public void setMovement(PigEntity pigEntity){
        pigEntity.setHeadYaw(0);
        pigEntity.prevHeadYaw = 0;
        pigEntity.bodyYaw = 0;
        pigEntity.prevBodyYaw = 0;
        pigEntity.setPitch(0);
        pigEntity.setYaw(0);
        pigEntity.prevYaw = 0;
        pigEntity.prevPitch = 0;
        pigEntity.age = 0; // 防止年龄动画
        pigEntity.limbAnimator.setSpeed(0); // 停止肢体动画
        pigEntity.handSwinging = false; // 停止手臂摆动
    }
}
