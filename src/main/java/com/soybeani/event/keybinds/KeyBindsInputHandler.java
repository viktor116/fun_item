package com.soybeani.event.keybinds;

import com.soybeani.config.InitValue;
import com.soybeani.network.packet.KeyRPacket;
import com.soybeani.network.packet.KeyVPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * @author soybean
 * @date 2024/10/22 14:26
 * @description
 */
public class KeyBindsInputHandler {

    public static final String KEY_TOGGLE_NAME = "key."+ InitValue.MOD_ID +".toggle";
    public static final String KEY_FUNCTION_NAME = "key."+ InitValue.MOD_ID +".function";
    public static final String KEY_CATEGORY_ID = "key.category."+ InitValue.MOD_ID +".soybean";

    public static KeyBinding KEY_R = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            KEY_TOGGLE_NAME, // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_R, // The keycode of the key
            KEY_CATEGORY_ID // The translation key of the keybinding's category.
    ));

    public static KeyBinding KEY_V = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            KEY_FUNCTION_NAME, // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_V, // The keycode of the key
            KEY_CATEGORY_ID // The translation key of the keybinding's category.
    ));

    public static void register(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KEY_R.wasPressed()) {
                ClientPlayNetworking.send(KeyRPacket.INSTANCE);
            }
            while (KEY_V.wasPressed()) {
                ClientPlayNetworking.send(KeyVPacket.INSTANCE);
            }
        });
    }
}
