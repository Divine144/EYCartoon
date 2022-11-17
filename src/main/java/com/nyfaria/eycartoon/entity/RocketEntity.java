package com.nyfaria.eycartoon.entity;


import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class RocketEntity extends Arrow implements IAnimatable {

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public RocketEntity(EntityType<? extends RocketEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(0, -0.1, 0);
        if (this.level instanceof ServerLevel level) {

            BlockPos result = blockTrace(this, ClipContext.Fluid.NONE, 125, true).getBlockPos().above();
            if (this.tickCount % 5 == 0) {
                level.sendParticles(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.5F), result.getX(), result.getY(), result.getZ(), 2, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        if (!this.level.isClientSide) {
            this.level.explode(null, entity.getX(), entity.getY(), entity.getZ(), 4.0F, Explosion.BlockInteraction.BREAK);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        BlockPos blockPos = pResult.getBlockPos();
        if (!this.level.isClientSide) {
            this.level.explode(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 4.0F, Explosion.BlockInteraction.BREAK);
        }
        this.discard();
    }

    @Override @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override @NotNull
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.METAL_HIT;
    }

    @Override
    protected float getWaterInertia() {
        return 1F;
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    private static BlockHitResult blockTrace(Entity livingEntity, ClipContext.Fluid rayTraceFluid, int range, boolean downOrFace) {
        Level level = livingEntity.level;
        ClipContext context;
        Vec3 start = new Vec3(livingEntity.getX(), livingEntity.getY() + livingEntity.getEyeHeight(), livingEntity.getZ());
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

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, p -> PlayState.STOP));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
