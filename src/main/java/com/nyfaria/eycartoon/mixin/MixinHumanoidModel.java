package com.nyfaria.eycartoon.mixin;

import com.nyfaria.eycartoon.items.CartoonRayItem;
import com.nyfaria.eycartoon.items.MinionLauncherItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class MixinHumanoidModel {
    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart head;

    @Shadow public HumanoidModel.ArmPose rightArmPose;

    @Shadow public HumanoidModel.ArmPose leftArmPose;

    @Inject(method = "poseRightArm(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "TAIL"))
    public <T extends LivingEntity> void poseRightArm(T pLivingEntity, CallbackInfo ci) {
        Item useItem = pLivingEntity.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (useItem instanceof CartoonRayItem) {
            this.rightArm.yRot = -0.1F + this.head.yRot;
            this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
            this.leftArm.xRot = 0;
            this.leftArm.yRot = 0;
        }
        else if (useItem instanceof MinionLauncherItem) {
            this.leftArm.yRot = 0.80F;
            this.leftArm.xRot = (-(float) Math.PI / 2F) + 0.2F;
            this.rightArm.yRot = -0.1F + this.head.yRot;
            this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
        }
    }

    @Inject(method = "poseLeftArm(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "TAIL"))
    public <T extends LivingEntity> void poseLeftArm(T pLivingEntity, CallbackInfo ci) {
        Item useItem = pLivingEntity.getItemInHand(InteractionHand.OFF_HAND).getItem();
        if (useItem instanceof CartoonRayItem) {
            this.leftArm.yRot = -0.1F + this.head.yRot;
            this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
            this.rightArm.xRot = 0;
            this.rightArm.yRot = 0;
        }
    }
}
