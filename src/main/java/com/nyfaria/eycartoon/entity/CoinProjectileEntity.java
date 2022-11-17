package com.nyfaria.eycartoon.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CoinProjectileEntity extends Arrow implements IAnimatable {

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private Vec3 initialPosition;

    public CoinProjectileEntity(EntityType<? extends Arrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level instanceof ServerLevel) {
            if (this.initialPosition != null) {
                if (Math.sqrt(this.distanceToSqr(initialPosition)) >= 9.0D) {
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        if (!this.level.isClientSide) {
            entity.hurt(DamageSource.GENERIC, 2F);
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.discard();
    }

    @Override @NotNull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isNoGravity() {
        return true;
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

    public void setInitialPosition(Vec3 position) {
        this.initialPosition = position;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
