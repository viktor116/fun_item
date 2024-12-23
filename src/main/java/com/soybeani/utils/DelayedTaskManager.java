package com.soybeani.utils;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author soybean
 * @date 2024/12/23 11:28
 * @description
 */
public class DelayedTaskManager {
    private static final Map<String, DelayedTask> activeTasks = new HashMap<>();
    private static boolean initialized = false;

    // 延迟任务类
    public static class DelayedTask {
        private int tickCounter = 0;
        private final int targetTicks;
        private final Runnable callback;
        private boolean completed = false;

        public DelayedTask(int targetTicks, Runnable callback) {
            this.targetTicks = targetTicks;
            this.callback = callback;
        }

        public void tick() {
            if (completed) return;

            tickCounter++;
            if (tickCounter >= targetTicks) {
                callback.run();
                completed = true;
            }
        }

        public boolean isCompleted() {
            return completed;
        }

        public void reset() {
            tickCounter = 0;
            completed = false;
        }
    }

    // 初始化管理器
    public static void init() {
        if (!initialized) {
            ServerTickEvents.END_SERVER_TICK.register(server -> {
                Iterator<Map.Entry<String, DelayedTask>> iterator = activeTasks.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, DelayedTask> entry = iterator.next();
                    DelayedTask task = entry.getValue();
                    task.tick();
                    if (task.isCompleted()) {
                        iterator.remove();
                    }
                }
            });
            initialized = true;
        }
    }

    // 注册新任务
    public static void scheduleTask(String taskId, int delayTicks, Runnable callback) {
        activeTasks.put(taskId, new DelayedTask(delayTicks, callback));
    }

    // 取消任务
    public static void cancelTask(String taskId) {
        activeTasks.remove(taskId);
    }

    // 重置任务
    public static void resetTask(String taskId) {
        DelayedTask task = activeTasks.get(taskId);
        if (task != null) {
            task.reset();
        }
    }

    // 检查任务是否存在
    public static boolean hasTask(String taskId) {
        return activeTasks.containsKey(taskId);
    }

    // 检查任务是否完成
    public static boolean isTaskCompleted(String taskId) {
        DelayedTask task = activeTasks.get(taskId);
        return task != null && task.isCompleted();
    }
}
