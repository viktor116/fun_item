package com.soybeani.config;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author soybean
 * @date 2024/10/8 15:46
 * @description
 */
public class InitValue {
    public static final Logger LOGGER = LoggerFactory.getLogger(InitValue.MOD_ID);
    public static final String MOD_ID = "fun_item";
    public static final Random RANDOM = new Random();
    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static Random getRandom(){
        return RANDOM;
    }
}
