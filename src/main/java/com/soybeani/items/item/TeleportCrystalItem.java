package com.soybeani.items.item;

import com.soybeani.utils.CommonUtils;
import com.soybeani.utils.RayCastUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/6 15:54
 * @description
 */
public class TeleportCrystalItem extends Item {
    private int type; //0为普通 1为高级

    private boolean block_tp = false; //0为关闭 1为开启

    public static final int TYPE_0 = 0;
    public static final int TYPE_1 = 1;


    public TeleportCrystalItem(Settings settings,int type) {
        super(settings);
        this.type = type;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Entity targetEntity = RayCastUtils.getTargetEntity(world, user, 200);
        ItemStack stackInHand = user.getStackInHand(hand);
        if (targetEntity != null) {
            Vec3d targetPos = targetEntity.getPos();
            BlockPos userBlockPos = user.getBlockPos();
            Entity targetVehicle = targetEntity.hasVehicle() ? targetEntity.getVehicle() : null;
            Entity userVehicle = user.hasVehicle() ? user.getVehicle() : null;

            // 如果目标实体有载具，移除目标实体的载具并将玩家设置为载具
            if (targetVehicle != null) {
                targetEntity.stopRiding();
            }

            // 交换目标实体的位置
            targetEntity.setPosition(user.getPos());

            // 将目标实体的乘坐者切换为玩家
            if (userVehicle != null) {
                user.stopRiding();
                targetEntity.startRiding(userVehicle);
            }

            // 交换玩家的位置
            user.setPosition(targetPos);

            // 如果玩家有载具，移除玩家的载具并将目标实体设置为载具
            if (targetVehicle != null) {
                user.stopRiding();
                targetEntity.startRiding(targetVehicle);
            }

            // 播放传送声音效果
            world.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);

            // 在服务器上生成粒子效果
            if (world instanceof ServerWorld serverWorld) {
                CommonUtils.spawnSelfParticle(serverWorld, user, ParticleTypes.PORTAL);
                CommonUtils.spawnSelfParticle(serverWorld, targetEntity, ParticleTypes.PORTAL);
                if (targetVehicle != null) {
                    CommonUtils.spawnSelfParticle(serverWorld, targetVehicle, ParticleTypes.PORTAL);
                }
                if (userVehicle != null) {
                    CommonUtils.spawnSelfParticle(serverWorld, userVehicle, ParticleTypes.PORTAL);
                }
            }
            if(this.getType() == TYPE_0){
                if(!user.isInCreativeMode()){
                    stackInHand.decrement(1);
                }
                user.getItemCooldownManager().set(this, 20);
            }else if(this.getType() == TYPE_1){
                stackInHand.damage(1,user, EquipmentSlot.MAINHAND);
                if(user.getOffHandStack().getItem() instanceof TalismanItem talismanItem){
                    ItemStack offHandStack = user.getOffHandStack();
                    if(talismanItem.getType() == TalismanItem.Type.BLUE){
                        TalismanItem.handleLightningSpell(user,userBlockPos,offHandStack);
                    }else if(talismanItem.getType() == TalismanItem.Type.GREEN){
                        TalismanItem.handleNatureHealing(user,offHandStack,1);
                    }else if(talismanItem.getType() == TalismanItem.Type.YELLOW_RED){
                        TalismanItem.handleFlameToEntity(user,targetEntity,offHandStack,100);
                    }
                }
                user.getItemCooldownManager().set(this, 20);
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        if(this.getType() == TYPE_1 && this.block_tp){
            HitResult raycast = user.raycast(200, 0, false);
            if(raycast != null && raycast.getType() == HitResult.Type.BLOCK){
                if (world instanceof ServerWorld serverWorld) {
                    CommonUtils.spawnSelfParticle(serverWorld, user, ParticleTypes.PORTAL);
                }
                BlockHitResult blockHitResult = (BlockHitResult) raycast;
                BlockPos userBlockPos = user.getBlockPos();
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = world.getBlockState(blockPos);
                world.setBlockState(userBlockPos,blockState);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                user.setPos(blockPos.getX(),blockPos.getY(),blockPos.getZ());
                // 播放传送声音效果
                world.playSound(null, userBlockPos.getX(), userBlockPos.getY(), userBlockPos.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
                world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
                if (world instanceof ServerWorld serverWorld) {
                    CommonUtils.spawnSelfParticle(serverWorld, user, ParticleTypes.PORTAL);
                }
                stackInHand.damage(1,user,EquipmentSlot.MAINHAND);
                user.getItemCooldownManager().set(this, 20);
            }
            return TypedActionResult.success(user.getStackInHand(hand));

        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    public void sendBlockTPMessage(PlayerEntity player){
         if(this.block_tp){
             player.sendMessage(Text.of("方块传送：开启"), true);
         } else {
             player.sendMessage(Text.of("方块传送：关闭"), true);
         }
    }


    public void switchBlockTP(){
        this.block_tp = !this.block_tp;
    }
    public int getType(){
        return this.type;
    }
}
