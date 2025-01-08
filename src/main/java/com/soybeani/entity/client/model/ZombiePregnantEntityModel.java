package com.soybeani.entity.client.model;

import com.google.common.collect.ImmutableList;
import com.soybeani.entity.custom.ZombiePregnantEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

/**
 * @author soybean
 * @date 2025/1/7 10:54
 * @description
 */
public class ZombiePregnantEntityModel extends AnimalModel<ZombiePregnantEntity> {
    private final ModelPart head;
    private final ModelPart headWear;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart belly;
    public BipedEntityModel.ArmPose leftArmPose;
    public BipedEntityModel.ArmPose rightArmPose;
    public boolean sneaking;
    public float leaningPitch;
    public ZombiePregnantEntityModel(ModelPart root) {
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.head = root.getChild("head");
        this.headWear = root.getChild("headwear");
        this.body = root.getChild("body");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
        this.belly = root.getChild("belly");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData headwear = modelPartData.addChild("headwear", ModelPartBuilder.create().uv(28, 46).cuboid(-4.5F, -8.0F, -4.5F, 9.0F, 9.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData belly = modelPartData.addChild("belly", ModelPartBuilder.create().uv(-2, 40).cuboid(-4.0F, -12.0F, -7.0F, 8.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        head.render(matrices, vertexConsumer, light, overlay, color);
        headWear.render(matrices, vertexConsumer, light, overlay, color);
        body.render(matrices, vertexConsumer, light, overlay,color);
        leftArm.render(matrices, vertexConsumer, light, overlay,color);
        rightArm.render(matrices, vertexConsumer, light, overlay,color);
        leftLeg.render(matrices, vertexConsumer, light, overlay,color);
        rightLeg.render(matrices, vertexConsumer, light, overlay,color);
        belly.render(matrices, vertexConsumer, light, overlay,color);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg, this.belly);
    }
    //float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch
    @Override
    public void setAngles(ZombiePregnantEntity entity,float f, float g, float h, float i, float j) {
        boolean bl = entity.getFallFlyingTicks() > 4;
        boolean bl2 = entity.isInSwimmingPose();
        this.head.yaw = i * 0.017453292F;
        if (bl) {
            this.head.pitch = -0.7853982F;
        } else if (this.leaningPitch > 0.0F) {
            if (bl2) {
                this.head.pitch = this.lerpAngle(this.leaningPitch, this.head.pitch, -0.7853982F);
            } else {
                this.head.pitch = this.lerpAngle(this.leaningPitch, this.head.pitch, j * 0.017453292F);
            }
        } else {
            this.head.pitch = j * 0.017453292F;
        }

        this.body.yaw = 0.0F;
        this.rightArm.pivotZ = 0.0F;
        this.rightArm.pivotX = -5.0F;
        this.leftArm.pivotZ = 0.0F;
        this.leftArm.pivotX = 5.0F;
        float k = 1.0F;
        if (bl) {
            k = (float)entity.getVelocity().lengthSquared();
            k /= 0.2F;
            k *= k * k;
        }

        if (k < 1.0F) {
            k = 1.0F;
        }

        this.rightArm.pitch = MathHelper.cos(f * 0.6662F + 3.1415927F) * 2.0F * g * 0.5F / k;
        this.leftArm.pitch = MathHelper.cos(f * 0.6662F) * 2.0F * g * 0.5F / k;
        this.rightArm.roll = 0.0F;
        this.leftArm.roll = 0.0F;
        this.rightLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g / k;
        this.leftLeg.pitch = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * g / k;
        this.rightLeg.yaw = 0.005F;
        this.leftLeg.yaw = -0.005F;
        this.rightLeg.roll = 0.005F;
        this.leftLeg.roll = -0.005F;
        ModelPart var10000;
        if (this.riding) {
            var10000 = this.rightArm;
            var10000.pitch += -0.62831855F;
            var10000 = this.leftArm;
            var10000.pitch += -0.62831855F;
            this.rightLeg.pitch = -1.4137167F;
            this.rightLeg.yaw = 0.31415927F;
            this.rightLeg.roll = 0.07853982F;
            this.leftLeg.pitch = -1.4137167F;
            this.leftLeg.yaw = -0.31415927F;
            this.leftLeg.roll = -0.07853982F;
        }

        this.rightArm.yaw = 0.0F;
        this.leftArm.yaw = 0.0F;
        boolean bl3 = entity.getMainArm() == Arm.RIGHT;
        boolean bl4;
        if (entity.isUsingItem()) {
            bl4 = entity.getActiveHand() == Hand.MAIN_HAND;
            if (bl4 == bl3) {
                this.positionRightArm(entity);
            } else {
                this.positionLeftArm(entity);
            }
        } else {
            bl4 = bl3 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
            if (bl3 != bl4) {
                this.positionLeftArm(entity);
                this.positionRightArm(entity);
            } else {
                this.positionRightArm(entity);
                this.positionLeftArm(entity);
            }
        }

        this.animateArms(entity, h);
        if (this.sneaking) {
            this.body.pitch = 0.5F;
            var10000 = this.rightArm;
            var10000.pitch += 0.4F;
            var10000 = this.leftArm;
            var10000.pitch += 0.4F;
            this.rightLeg.pivotZ = 4.0F;
            this.leftLeg.pivotZ = 4.0F;
            this.rightLeg.pivotY = 12.2F;
            this.leftLeg.pivotY = 12.2F;
            this.head.pivotY = 4.2F;
            this.body.pivotY = 3.2F;
            this.leftArm.pivotY = 5.2F;
            this.rightArm.pivotY = 5.2F;
        } else {
            this.body.pitch = 0.0F;
            this.rightLeg.pivotZ = 0.0F;
            this.leftLeg.pivotZ = 0.0F;
            this.rightLeg.pivotY = 12.0F;
            this.leftLeg.pivotY = 12.0F;
            this.head.pivotY = 0.0F;
            this.body.pivotY = 0.0F;
            this.leftArm.pivotY = 2.0F;
            this.rightArm.pivotY = 2.0F;
        }

        if (this.rightArmPose != BipedEntityModel.ArmPose.SPYGLASS) {
            CrossbowPosing.swingArm(this.rightArm, h, 1.0F);
        }

        if (this.leftArmPose != BipedEntityModel.ArmPose.SPYGLASS) {
            CrossbowPosing.swingArm(this.leftArm, h, -1.0F);
        }

        if (this.leaningPitch > 0.0F) {
            float l = f % 26.0F;
            Arm arm = this.getPreferredArm(entity);
            float m = arm == Arm.RIGHT && this.handSwingProgress > 0.0F ? 0.0F : this.leaningPitch;
            float n = arm == Arm.LEFT && this.handSwingProgress > 0.0F ? 0.0F : this.leaningPitch;
            float o;
            if (!entity.isUsingItem()) {
                if (l < 14.0F) {
                    this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, 0.0F);
                    this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, 0.0F);
                    this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, 3.1415927F);
                    this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, 3.1415927F);
                    this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, 3.1415927F + 1.8707964F * this.method_2807(l) / this.method_2807(14.0F));
                    this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, 3.1415927F - 1.8707964F * this.method_2807(l) / this.method_2807(14.0F));
                } else if (l >= 14.0F && l < 22.0F) {
                    o = (l - 14.0F) / 8.0F;
                    this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, 1.5707964F * o);
                    this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, 1.5707964F * o);
                    this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, 3.1415927F);
                    this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, 3.1415927F);
                    this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, 5.012389F - 1.8707964F * o);
                    this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, 1.2707963F + 1.8707964F * o);
                } else if (l >= 22.0F && l < 26.0F) {
                    o = (l - 22.0F) / 4.0F;
                    this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, 1.5707964F - 1.5707964F * o);
                    this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, 1.5707964F - 1.5707964F * o);
                    this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, 3.1415927F);
                    this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, 3.1415927F);
                    this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, 3.1415927F);
                    this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, 3.1415927F);
                }
            }

            o = 0.3F;
            float p = 0.33333334F;
            this.leftLeg.pitch = MathHelper.lerp(this.leaningPitch, this.leftLeg.pitch, 0.3F * MathHelper.cos(f * 0.33333334F + 3.1415927F));
            this.rightLeg.pitch = MathHelper.lerp(this.leaningPitch, this.rightLeg.pitch, 0.3F * MathHelper.cos(f * 0.33333334F));
        }
        CrossbowPosing.meleeAttack(this.leftArm, this.rightArm, this.isAttacking(entity), this.handSwingProgress, h);
    }

    protected float lerpAngle(float angleOne, float angleTwo, float magnitude) {
        float f = (magnitude - angleTwo) % 6.2831855F;
        if (f < -3.1415927F) {
            f += 6.2831855F;
        }

        if (f >= 3.1415927F) {
            f -= 6.2831855F;
        }

        return angleTwo + angleOne * f;
    }

    private void positionRightArm(ZombiePregnantEntity entity) {
        switch (this.rightArmPose.ordinal()) {
            case 0:
                this.rightArm.yaw = 0.0F;
                break;
            case 1:
                this.rightArm.pitch = this.rightArm.pitch * 0.5F - 0.31415927F;
                this.rightArm.yaw = 0.0F;
                break;
            case 2:
                this.positionBlockingArm(this.rightArm, true);
                break;
            case 3:
                this.rightArm.yaw = -0.1F + this.head.yaw;
                this.leftArm.yaw = 0.1F + this.head.yaw + 0.4F;
                this.rightArm.pitch = -1.5707964F + this.head.pitch;
                this.leftArm.pitch = -1.5707964F + this.head.pitch;
                break;
            case 4:
                this.rightArm.pitch = this.rightArm.pitch * 0.5F - 3.1415927F;
                this.rightArm.yaw = 0.0F;
                break;
            case 5:
                CrossbowPosing.charge(this.rightArm, this.leftArm, entity, true);
                break;
            case 6:
                CrossbowPosing.hold(this.rightArm, this.leftArm, this.head, true);
                break;
            case 7:
                this.rightArm.pitch = MathHelper.clamp(this.head.pitch - 1.9198622F - (entity.isInSneakingPose() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
                this.rightArm.yaw = this.head.yaw - 0.2617994F;
                break;
            case 8:
                this.rightArm.pitch = MathHelper.clamp(this.head.pitch, -1.2F, 1.2F) - 1.4835298F;
                this.rightArm.yaw = this.head.yaw - 0.5235988F;
                break;
            case 9:
                this.rightArm.pitch = this.rightArm.pitch * 0.5F - 0.62831855F;
                this.rightArm.yaw = 0.0F;
        }

    }

    private void positionLeftArm(ZombiePregnantEntity entity) {
        switch (this.leftArmPose.ordinal()) {
            case 0:
                this.leftArm.yaw = 0.0F;
                break;
            case 1:
                this.leftArm.pitch = this.leftArm.pitch * 0.5F - 0.31415927F;
                this.leftArm.yaw = 0.0F;
                break;
            case 2:
                this.positionBlockingArm(this.leftArm, false);
                break;
            case 3:
                this.rightArm.yaw = -0.1F + this.head.yaw - 0.4F;
                this.leftArm.yaw = 0.1F + this.head.yaw;
                this.rightArm.pitch = -1.5707964F + this.head.pitch;
                this.leftArm.pitch = -1.5707964F + this.head.pitch;
                break;
            case 4:
                this.leftArm.pitch = this.leftArm.pitch * 0.5F - 3.1415927F;
                this.leftArm.yaw = 0.0F;
                break;
            case 5:
                CrossbowPosing.charge(this.rightArm, this.leftArm, entity, false);
                break;
            case 6:
                CrossbowPosing.hold(this.rightArm, this.leftArm, this.head, false);
                break;
            case 7:
                this.leftArm.pitch = MathHelper.clamp(this.head.pitch - 1.9198622F - (entity.isInSneakingPose() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
                this.leftArm.yaw = this.head.yaw + 0.2617994F;
                break;
            case 8:
                this.leftArm.pitch = MathHelper.clamp(this.head.pitch, -1.2F, 1.2F) - 1.4835298F;
                this.leftArm.yaw = this.head.yaw + 0.5235988F;
                break;
            case 9:
                this.leftArm.pitch = this.leftArm.pitch * 0.5F - 0.62831855F;
                this.leftArm.yaw = 0.0F;
        }

    }

    private void positionBlockingArm(ModelPart arm, boolean rightArm) {
        arm.pitch = arm.pitch * 0.5F - 0.9424779F + MathHelper.clamp(this.head.pitch, -1.3962634F, 0.43633232F);
        arm.yaw = (rightArm ? -30.0F : 30.0F) * 0.017453292F + MathHelper.clamp(this.head.yaw, -0.5235988F, 0.5235988F);
    }

    private Arm getPreferredArm(ZombiePregnantEntity entity) {
        Arm arm = entity.getMainArm();
        return entity.preferredHand == Hand.MAIN_HAND ? arm : arm.getOpposite();
    }

    protected void animateArms(ZombiePregnantEntity entity, float animationProgress) {
        if (!(this.handSwingProgress <= 0.0F)) {
            Arm arm = this.getPreferredArm(entity);
            ModelPart modelPart = this.getArm(arm);
            float f = this.handSwingProgress;
            this.body.yaw = MathHelper.sin(MathHelper.sqrt(f) * 6.2831855F) * 0.2F;
            ModelPart var10000;
            if (arm == Arm.LEFT) {
                var10000 = this.body;
                var10000.yaw *= -1.0F;
            }

            this.rightArm.pivotZ = MathHelper.sin(this.body.yaw) * 5.0F;
            this.rightArm.pivotX = -MathHelper.cos(this.body.yaw) * 5.0F;
            this.leftArm.pivotZ = -MathHelper.sin(this.body.yaw) * 5.0F;
            this.leftArm.pivotX = MathHelper.cos(this.body.yaw) * 5.0F;
            var10000 = this.rightArm;
            var10000.yaw += this.body.yaw;
            var10000 = this.leftArm;
            var10000.yaw += this.body.yaw;
            var10000 = this.leftArm;
            var10000.pitch += this.body.yaw;
            f = 1.0F - this.handSwingProgress;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float g = MathHelper.sin(f * 3.1415927F);
            float h = MathHelper.sin(this.handSwingProgress * 3.1415927F) * -(this.head.pitch - 0.7F) * 0.75F;
            modelPart.pitch -= g * 1.2F + h;
            modelPart.yaw += this.body.yaw * 2.0F;
            modelPart.roll += MathHelper.sin(this.handSwingProgress * 3.1415927F) * -0.4F;
        }
    }
    protected ModelPart getArm(Arm arm) {
        return arm == Arm.LEFT ? this.leftArm : this.rightArm;
    }

    private float method_2807(float f) {
        return -65.0F * f + f * f;
    }
    public boolean isAttacking(ZombiePregnantEntity entity) {
        return entity.isAttacking();
    }
}

