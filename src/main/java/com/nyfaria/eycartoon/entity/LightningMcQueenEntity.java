package com.nyfaria.eycartoon.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
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
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class LightningMcQueenEntity extends Animal implements IAnimatable, PlayerRideable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private Vec3 lastPosition;

    public LightningMcQueenEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 5, p -> PlayState.CONTINUE));
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
       if (!this.level.isClientSide) {
           if (canAddPassenger(pPlayer)) {
               return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
           }
           return InteractionResult.PASS;
       }
       else {
           return InteractionResult.SUCCESS;
       }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource != DamageSource.OUT_OF_WORLD;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) return;
        if (lastPosition != null) {
            BlockPos lastPos = new BlockPos(lastPosition);
            if (BaseFireBlock.canBePlacedAt(level, new BlockPos(lastPosition), Direction.NORTH)) {
                BlockState state = BaseFireBlock.getState(level, lastPos);
                level.setBlock(lastPos, state, 11);
            }
        }
        lastPosition = position();
    }

    @Override
    public boolean isPickable() {
        return isAlive();
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return getPassengers().isEmpty();
    }

    @Override
    public void positionRider(Entity pPassenger) {
        int angle = -90;
        Vec3 position = position();
        Vec3 direction = calculateViewVector(0, getYRot() + angle).normalize();
        Vec3 riderPosition = position.add(direction);
        pPassenger.setPos(riderPosition.x, riderPosition.y + getPassengersRidingOffset(), riderPosition.z);
        pPassenger.setYBodyRot(getYRot());
    }

    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.25f;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (isAlive()) {
            Entity controller = getControllingPassenger();
            if (isVehicle() && controller instanceof LivingEntity living) {
                setYRot(living.getYRot());
                yRotO = getYRot();
                setXRot(living.getXRot() * 0.5F);
                setRot(getYRot(), getXRot());
                yBodyRot = getYRot();
                yHeadRot = yBodyRot;
                float f = 0;
                float f1 = living.zza * getSpeed();
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }
                flyingSpeed = getSpeed() * 0.1F;
                if (isControlledByLocalInstance()) {
                    setSpeed( (float) getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vec3(f, pTravelVector.y, f1));
                }
                else if (living instanceof Player) {
                    setDeltaMovement(Vec3.ZERO);
                }
                tryCheckInsideBlocks();
            }
            else {
                flyingSpeed = 0.02F;
                super.travel(pTravelVector);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, 0.675D).add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 0.6d);
    }

    @Override
    public float getSpeed() {
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED) * 2.2F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }
}
