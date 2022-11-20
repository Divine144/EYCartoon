package com.nyfaria.eycartoon.entity;

import com.nyfaria.eycartoon.entity.goal.MegaSnowGolemAttackGoal;
import com.nyfaria.eycartoon.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
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
import java.util.Optional;


public class MegaSnowGolemEntity extends AbstractGolem implements RangedAttackMob, IAnimatable {

    private static final EntityDataAccessor<Boolean> SHOULD_ATTACK = SynchedEntityData.defineId(MegaSnowGolemEntity.class, EntityDataSerializers.BOOLEAN);

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MegaSnowGolemEntity(EntityType<? extends AbstractGolem> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MegaSnowGolemAttackGoal(this, 0.65D, 25, 25, 20.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.65D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, p -> true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOULD_ATTACK, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.entityData.set(SHOULD_ATTACK, pCompound.getBoolean("attack"));
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("attack", this.entityData.get(SHOULD_ATTACK));
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) return;
        if (this.getTarget() == null) this.setShouldAttack(false);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY());
            int k = Mth.floor(this.getZ());
            BlockPos blockpos = new BlockPos(i, j, k);
            Biome biome = this.level.getBiome(blockpos).value();
            if (biome.shouldSnowGolemBurn(blockpos)) {
                this.hurt(DamageSource.ON_FIRE, 1.0F);
            }
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }
            BlockState blockstate = Blocks.SNOW.defaultBlockState();
            for(int l = 0; l < 4; ++l) {
                i = Mth.floor(this.getX() + (double)((float)(l % 2 * 2 - 1) * 0.25F));
                j = Mth.floor(this.getY());
                k = Mth.floor(this.getZ() + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos1 = new BlockPos(i, j, k);
                if (this.level.isEmptyBlock(blockpos1) && blockstate.canSurvive(this.level, blockpos1)) {
                    this.level.setBlockAndUpdate(blockpos1, blockstate);
                    this.level.gameEvent(GameEvent.BLOCK_PLACE, blockpos1, GameEvent.Context.of(this, blockstate));
                }
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        LargeSnowball snowball = new LargeSnowball(EntityInit.LARGE_SNOWBALL.get(), this.level);
        snowball.setOwner(this);
        Vec3 viewVector = this.getViewVector(1.0F);
        snowball.setPos(this.getX() + viewVector.x, this.getY() + 1, this.getZ() + viewVector.z);
        double d0 = pTarget.getEyeY() - (double)1.1F;
        double d1 = pTarget.getX() - this.getX();
        double d2 = d0 - snowball.getY();
        double d3 = pTarget.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(snowball);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SNOW_GOLEM_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.SNOW_GOLEM_DEATH;
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
        if (this.getShouldAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle2", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 1D).add(Attributes.ATTACK_DAMAGE, 1D).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    public boolean getShouldAttack() {
        return this.entityData.get(SHOULD_ATTACK);
    }

    public void setShouldAttack(boolean shouldHeadbutt) {
        this.entityData.set(SHOULD_ATTACK, shouldHeadbutt);
    }
}
