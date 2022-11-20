package com.nyfaria.eycartoon.entity;

import com.nyfaria.eycartoon.entity.goal.BossBabyFollowGoal;
import com.nyfaria.eycartoon.entity.goal.BossBabyTargetOwnerGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
import java.util.Optional;
import java.util.UUID;


public class BossBabyEntity extends PathfinderMob implements IAnimatable {

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final EntityDataAccessor<Boolean> SHOULD_HEADBUTT_ANIMATION = SynchedEntityData.defineId(BossBabyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(BossBabyEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public BossBabyEntity(EntityType<? extends BossBabyEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BossBabyEntity(EntityType<? extends BossBabyEntity> pEntityType, Level pLevel, Player owner) {
        this(pEntityType, pLevel);
        this.entityData.set(DATA_OWNERUUID_ID, Optional.of(owner.getUUID()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOULD_HEADBUTT_ANIMATION, false);
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.entityData.set(SHOULD_HEADBUTT_ANIMATION, pCompound.getBoolean("headbutt"));
        if (pCompound.contains("Owner")) {
            this.entityData.set(DATA_OWNERUUID_ID, Optional.of(pCompound.getUUID("Owner")));
        }
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("headbutt", this.entityData.get(SHOULD_HEADBUTT_ANIMATION));
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Spider.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MegaSnowGolemEntity.class, true));
        this.goalSelector.addGoal(0, new BossBabyTargetOwnerGoal(this, false));
        this.goalSelector.addGoal(1, new BossBabyFollowGoal(this, this.getOwner(), 0.7F, 8F, 2F, false));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.6F, false) {
            @Override
            protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
                double d0 = this.getAttackReachSqr(pEnemy);
                if (pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
                    this.resetAttackCooldown();
                    BossBabyEntity.this.setShouldHeadbutt(true);
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(pEnemy);
                }
            }
        });
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 10F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7F));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_KNOCKBACK, 5).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Nullable
    public Player getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.of(ownerUUID));
    }

    public boolean getShouldHeadbutt() {
        return this.entityData.get(SHOULD_HEADBUTT_ANIMATION);
    }

    public void setShouldHeadbutt(boolean shouldHeadbutt) {
        this.entityData.set(SHOULD_HEADBUTT_ANIMATION, shouldHeadbutt);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationEvent));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <T extends IAnimatable> PlayState animationEvent(AnimationEvent<T> event) {
        if (this.getShouldHeadbutt()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("much", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.setShouldHeadbutt(false);
        }
        else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }
}
