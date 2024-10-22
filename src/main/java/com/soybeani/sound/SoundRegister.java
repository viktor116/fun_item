package com.soybeani.sound;

import com.soybeani.config.InitValue;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/21 17:00
 * @description
 */
public class SoundRegister {
    public static final SoundEvent LEI_JUN = register("lei_jun");

    public static void initialize(){

    }

    public static SoundEvent register(String id){
        Identifier soundID = Identifier.of(InitValue.MOD_ID, id);
        SoundEvent soundEvent = SoundEvent.of(soundID);
        return Registry.register(Registries.SOUND_EVENT, soundID, soundEvent);
    }
}
