package com.soybeani.items.item;

import com.soybeani.config.InitValue;
import com.soybeani.entity.client.renderer.GatlingRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author soybean
 * @date 2024/12/27 14:51
 * @description
 */
public class GatlingItem extends RangedWeaponItem implements GeoItem {
    private static final RawAnimation ACTIVATE_SHOOT = RawAnimation.begin().then("shoot", Animation.LoopType.LOOP);
    private static final RawAnimation ACTIVATE_IDLE= RawAnimation.begin().then("idea", Animation.LoopType.LOOP);

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final Predicate<ItemStack> BOW_PROJECTILES = (stack) -> {
        return stack.isIn(ItemTags.ARROWS);
    };
    public static final int RANGE = 100;
    public GatlingItem(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoItem>(this, "controller", 0, state -> PlayState.CONTINUE).triggerableAnim("idle", ACTIVATE_IDLE).triggerableAnim("shoot", ACTIVATE_SHOOT));
    }

//    private PlayState predicate(AnimationState<GeoItem> animationState) {
//
//        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
//        animationState.getController().setAnimation(ACTIVATE_SHOOT);
//        return PlayState.CONTINUE;
//    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(entity instanceof PlayerEntity player){
//            triggerAnim(player, GeoItem.getId(stack),"controller", "idle");
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (world instanceof ServerWorld serverWorld){
            triggerAnim(player, GeoItem.getOrAssignId(player.getStackInHand(hand), serverWorld), "controller", "shoot");
        }
        return super.use(world, player, hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GatlingRenderer renderer;

            @Override
            public @Nullable BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new GatlingRenderer();
                return this.renderer;
            }
        });
    }
}
