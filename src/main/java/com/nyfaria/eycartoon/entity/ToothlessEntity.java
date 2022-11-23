package com.nyfaria.eycartoon.entity;

import com.nyfaria.hmutility.utils.HMUVectorUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
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

public class ToothlessEntity extends PathfinderMob implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final EntityDataAccessor<Boolean> FLIGHT = SynchedEntityData.defineId(ToothlessEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FLIGHT_TICKS = SynchedEntityData.defineId(ToothlessEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> WARP = SynchedEntityData.defineId(ToothlessEntity.class, EntityDataSerializers.BOOLEAN);

    public ToothlessEntity(EntityType<? extends ToothlessEntity> pEntityType, Level pLevel) {
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLIGHT, false);
        this.entityData.define(WARP, false);
        this.entityData.define(FLIGHT_TICKS, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && isWarp()) {
            if (this.getControllingPassenger() != null) {
                BlockHitResult hr = blockTrace(this.getControllingPassenger(), ClipContext.Fluid.NONE, 200, false);
                BlockPos above = hr.getBlockPos();
                BlockPos blockpos1 = above.relative(hr.getDirection());
                if (BaseFireBlock.canBePlacedAt(level, blockpos1, Direction.NORTH)) {
                    BlockState state1 = BaseFireBlock.getState(level, blockpos1);
                    level.setBlock(blockpos1, state1, 11);
                }
            }
        }
    }

    public void setFlightTicks(int ticks) {
        this.entityData.set(FLIGHT_TICKS, ticks);
    }

    public int getFlightTicks() {
        return this.entityData.get(FLIGHT_TICKS);
    }

    public void setFlight(boolean flight) {
        this.entityData.set(FLIGHT, flight);
        setFlightTicks(flight ? 53 : 17);
    }

    public boolean canFly() {
        return this.entityData.get(FLIGHT);
    }

    public boolean isWarp() {
        return this.entityData.get(WARP);
    }

    public void setWarp(boolean warp) {
        this.entityData.set(WARP, warp);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return passenger instanceof Player && getPassengers().isEmpty();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "controller_two", 0, this::openMouthPredicate));
    }

    private <T extends IAnimatable> PlayState openMouthPredicate(AnimationEvent<T> tAnimationEvent) {
        if (this.isWarp()) {
            tAnimationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("opened mouth", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> tAnimationEvent) {
        if (!isVehicle()) {
            tAnimationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else {
            tAnimationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("fly", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Nullable
    @Override
    public Player getControllingPassenger() {
        return (Player) getFirstPassenger();
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
    public boolean isAlwaysTicking() {
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


    private void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {
            double d0 = this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset();
            pCallback.accept(pPassenger, this.getX(), d0, this.getZ());
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return getBbHeight();
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isAlive()) {
            LivingEntity livingentity = this.getControllingPassenger();
            if (this.isVehicle() && livingentity != null) {
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
                    super.travel(new Vec3((double) strafe, pTravelVector.y, (double) forward));
                } else if (livingentity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }


                this.calculateEntityAnimation(this, false);
                this.tryCheckInsideBlocks();
            }
        }
    }

    public static BlockHitResult blockTrace(LivingEntity livingEntity, ClipContext.Fluid rayTraceFluid, int range, boolean downOrFace) {
        Level level = livingEntity.level;
        ClipContext context;

        double posY = livingEntity.getY();
        if (livingEntity.getVehicle() != null) {
            posY = livingEntity.getVehicle().getY();
        }

        Vec3 start = new Vec3(livingEntity.getX(), posY + livingEntity.getEyeHeight(), livingEntity.getZ());
        Vec3 look;

        if (!downOrFace) {
            look = livingEntity.getLookAngle();
        } else {
            look = new Vec3(0, -range, 0);
        }
        Vec3 end = new Vec3(livingEntity.getX() + look.x * (double) range, livingEntity.getY() + livingEntity.getEyeHeight() + look.y * (double) range, livingEntity.getZ() + look.z * (double) range);
        context = new ClipContext(start, end, ClipContext.Block.COLLIDER, rayTraceFluid, livingEntity);
        return level.clip(context);
    }
}
