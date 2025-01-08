package com.soybeani.items.item;

import com.soybeani.entity.vehicle.FlyBoatEntity;
import com.soybeani.entity.vehicle.TntBoatEntity;
import com.soybeani.event.keybinds.KeyBindsInputHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/6 15:37
 * @description
 */
public class TntBatonItem extends Item {
    public static final int MAX_MODE = 5 + 1;
    private int current_mode = 0;

    public TntBatonItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        HitResult hitResult = user.raycast(200, 0, true);
        Vec3d pos = hitResult.getPos();
        ItemStack stackInHand = user.getStackInHand(hand);
        int currentMode = this.current_mode;
        switch(currentMode) {
            case 0:
                this.sendKeyBindMessage(user);
                break;
            case 1:
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    world.createExplosion(user, pos.x, pos.y, pos.z, 4.0f, true, World.ExplosionSourceType.TNT);
                    stackInHand.damage(10,user, EquipmentSlot.MAINHAND);
                    return TypedActionResult.success(user.getStackInHand(hand));
                }
                break;
            case 2: //指挥
                if(this.validHasTntBoat(user)){
                    TntBoatEntity boat = (TntBoatEntity) user.getVehicle();
                    boat.launchTntAtTarget(user,pos);
                }else{
                    this.sendNeedTNTBoat(user);
                }
                break;
            case 3://全自动
                if(this.validHasTntBoat(user)){
                    TntBoatEntity boat = (TntBoatEntity) user.getVehicle();
                    boat.setAutoMode(true);
                }else {
                    this.sendNeedTNTBoat(user);
                }
                break;
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public void sendKeyBindMessage(PlayerEntity player) {
        // 获取 Minecraft 客户端实例
        MinecraftClient client = MinecraftClient.getInstance();
        // 获取 KEY_R 的绑定按键
        KeyBinding keyBinding = KeyBindsInputHandler.KEY_R;
        // 获取并发送快捷键名称（没有其他的附加信息）
        String keyName = keyBinding.getBoundKeyLocalizedText().getString();
        // 使用本地化后的快捷键名称发送消息
        player.sendMessage(Text.of("请使用快捷键 " + keyName + " 切换模式使用"), true);
    }

    public void sendSwitchModeMessage(PlayerEntity player){
        int currentMode = this.current_mode;
        switch(currentMode){
            case 0 -> player.sendMessage(Text.of("当前模式: 无"), true);
            case 1 -> player.sendMessage(Text.of("当前模式: 定点引爆"), true);
            case 2 -> player.sendMessage(Text.of("当前模式: 指挥"), true);
            case 3 -> player.sendMessage(Text.of("当前模式: 全自动"), true);
            case 4 -> player.sendMessage(Text.of("当前模式: 随机"), true);
            case 5 -> player.sendMessage(Text.of("当前模式: 随机全自动"), true);
        }
    }

    public void sendNeedTNTBoat(PlayerEntity player){
        player.sendMessage(Text.of("需要配合TNT船进行使用"), true);
    }

    public boolean validHasTntBoat(PlayerEntity player){
        boolean b = player.hasVehicle();
        if(b){
            return player.getVehicle() instanceof TntBoatEntity;
        }
        return false;
    }
    public void setCurrentMode(int mode){
        this.current_mode = mode;
    }

    public int getCurrentMode(){
        return this.current_mode;
    }

    public void switchMode(){
        this.current_mode = (this.current_mode + 1) % MAX_MODE;
    }
}
