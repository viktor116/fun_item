package com.soybeani.mixin;


import com.soybeani.items.ItemsRegister;
import com.soybeani.items.item.TalismanItem;
import com.soybeani.utils.DelayedTaskManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        Item item = (Item)(Object) this;
        if(item == Items.WOODEN_SWORD && user.getOffHandStack().getItem() instanceof TalismanItem talismanItem){
            if(!world.isClient){
                //符禄剑气
                TalismanItem.Type type = talismanItem.getType();
                if(type == TalismanItem.Type.NONE){
                    cir.cancel();
                    talismanItem.spawnSwordSlashParticles(user,world,talismanItem.getDefaultStack(),5);
                    cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
                }else if(type == TalismanItem.Type.BLACK_PURPLE){
                    if(user instanceof ServerPlayerEntity serverPlayer) {

                        if(!user.isInCreativeMode()){
                            ItemStack offHandStack = user.getOffHandStack();
                            offHandStack.decrement(1);
                        }
                        // 生成任务ID
                        String taskId = "blackPurple_" + user.getUuid().toString();

                        // 判定效果类型
                        boolean isPlayerTarget = world.random.nextBoolean();
                        world.setRainGradient(10.0F);
                        // 先发送提示消息
                        if(isPlayerTarget) {
                            serverPlayer.sendMessage(Text.literal("§5不详的预感...").formatted(Formatting.BOLD), false);
                            world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
                                player.sendMessage(Text.literal("§d" + user.getName().getString() + " 触怒了神明...").formatted(Formatting.BOLD), false);
                            });
                        } else {
                            serverPlayer.sendMessage(Text.literal("§5神秘的力量即将降临...").formatted(Formatting.BOLD), false);
                            world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
                                player.sendMessage(Text.literal("§d" + user.getName().getString() + " 正在召唤天罚...").formatted(Formatting.BOLD), false);
                            });
                        }

                        // 创建延迟任务（60 ticks = 3秒）
                        DelayedTaskManager.scheduleTask(taskId, 60, () -> {
                            if(isPlayerTarget) {
                                // 在玩家身上生成闪电
                                LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                                lightning.setPos(user.getX(), user.getY(), user.getZ());
                                world.spawnEntity(lightning);
                                // 击杀玩家
                                user.damage(world.getDamageSources().lightningBolt(), Float.MAX_VALUE);

                                // 发送数据包给客户端显示凋零骷髅HUD
                                PacketByteBuf buf = PacketByteBufs.create();
                                buf.writeBoolean(true); // true表示显示凋零骷髅HUD
//                                 ServerPlayNetworking.send(serverPlayer, Identifier.of("yourmod", "display_kill_hud"), buf);
                                // 发送结果消息
                                world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
                                    player.sendMessage(Text.literal("§d神明的惩罚降临了！").formatted(Formatting.BOLD), false);
                                });
                            } else {
                                // 检测50格范围内的所有生物
                                Box box = new Box(
                                        user.getX() - 50,
                                        user.getY() - 50,
                                        user.getZ() - 50,
                                        user.getX() + 50,
                                        user.getY() + 50,
                                        user.getZ() + 50
                                );

                                List<LivingEntity> entities = world.getEntitiesByClass(
                                        LivingEntity.class,
                                        box,
                                        entity -> entity != user && !entity.isSpectator()
                                );

                                if(!entities.isEmpty()) {
                                    for (LivingEntity target : entities) {
                                        // 生成闪电
                                        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                                        lightning.setPos(target.getX(), target.getY(), target.getZ());
                                        world.spawnEntity(lightning);
                                        // 直接击杀目标
                                        target.damage(world.getDamageSources().lightningBolt(), Float.MAX_VALUE);
                                    }

                                    // 发送数据包给客户端显示不死图腾HUD
                                    PacketByteBuf buf = PacketByteBufs.create();
                                    buf.writeBoolean(false); // false表示显示不死图腾HUD
                                    // ServerPlayNetworking.send(serverPlayer, Identifier.of("yourmod", "display_kill_hud"), buf);
                                    // 发送结果消息
                                    world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
                                        player.sendMessage(Text.literal("§d天罚降临，周围生物尽数灭亡！").formatted(Formatting.BOLD), false);
                                    });
                                }
                            }
                        });
                    }

                    cir.cancel();
                    cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
                }else if(type == TalismanItem.Type.PURPLE){
                    if(user instanceof ServerPlayerEntity serverPlayer) {
                        // 扣除玩家一半生命值
                        float currentHealth = user.getHealth();
                        user.setHealth(currentHealth / 2);
                        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_HURT_DROWN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        if(!user.isInCreativeMode()){
                            ItemStack offHandStack = user.getOffHandStack();
                            offHandStack.decrement(1);
                        }
                        TalismanItem.StoreEffectDAO storeEffectDAO = talismanItem.activePurpleEffects.get(user.getUuid());
                        int level = 1;
                        if(storeEffectDAO != null){
                            level = storeEffectDAO.getLevel() + 1;
                        }
                        talismanItem.activePurpleEffects.put(user.getUuid(), new TalismanItem.StoreEffectDAO(level,20*30*2)); // 10秒持续时间

                    }
                    cir.cancel();
                    cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
                }else if(type == TalismanItem.Type.YELLOW){
                    if(user instanceof ServerPlayerEntity serverPlayer) {
                        ItemStack gunStack = new ItemStack(ItemsRegister.GATLING_GUN);
                        user.setStackInHand(Hand.MAIN_HAND, gunStack);

                        if(!user.isInCreativeMode()){
                            ItemStack offHandStack = user.getOffHandStack();
                            offHandStack.decrement(1);
                        }
                    }
                    cir.cancel();
                    cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
                }
            }
        }
    }
}
