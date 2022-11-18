package com.nyfaria.eycartoon.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

public class ToothlessEntity extends Animal implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final EntityDataAccessor<Boolean> WARP = SynchedEntityData.defineId(ToothlessEntity.class, EntityDataSerializers.BOOLEAN);

    public ToothlessEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        maxUpStep = 1.5F;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.level.isClientSide) {
            pPlayer.startRiding(this);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WARP, false);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isAlive()) {
            Entity entity = getControllingPassenger();
            if (this.isVehicle() && entity instanceof LivingEntity livingentity) {
                this.fallDistance = 0;
                this.setYRot(livingentity.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(livingentity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float strafe = livingentity.xxa * 0.5F;
                float forward = livingentity.zza;
                if (forward <= 0.0F) {
                    forward *= 0.25F;
                }
                this.flyingSpeed = this.getSpeed();
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    this.flyingSpeed = this.getSpeed();
                    super.travel(new Vec3(strafe, pTravelVector.y, forward));
                }
                else if (livingentity instanceof Player){
                    this.setDeltaMovement(Vec3.ZERO);
                }
                this.calculateEntityAnimation(this, false);
            }
        }
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return getPassengers().isEmpty();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset();
    }

    @Override
    public void positionRider(Entity pPassenger) {
        this.positionRider(pPassenger, Entity::setPos);
    }

    @Override
    public double getPassengersRidingOffset() {
        return getBbHeight();
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100D).add(Attributes.ATTACK_DAMAGE);
    }

    public boolean isWarp() {
        return this.entityData.get(WARP);
    }

    public void setWarp(boolean warp) {
        this.entityData.set(WARP, warp);
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> tAnimationEvent) {
        if (!isVehicle()) {
            tAnimationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else {
            tAnimationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("fly",  ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {
            double d0 = this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset();
            pCallback.accept(pPassenger, this.getX(), d0, this.getZ());
        }
    }
}
