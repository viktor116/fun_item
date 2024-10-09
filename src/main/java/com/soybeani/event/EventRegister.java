package com.soybeani.event;

import com.soybeani.event.entity.EventAttack;
import com.soybeani.event.entity.EventUse;

/**
 * @author soybean
 * @date 2024/10/9 15:18
 * @description 总事件
 */
public class EventRegister {
    public static void initialize() {
        EventAttack.register();
        EventUse.register();
    }
}
