package com.nyfaria.eycartoon.entity;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MinionProjectileEntity extends Arrow implements IAnimatable {

    protected AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MinionProjectileEntity(EntityType<? extends MinionProjectileEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final DustParticleOptions YELLOW = new DustParticleOptions(new Vector3f(Vec3.fromRGB24(14737920)), 1.2F);

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) return;
        if (!(this instanceof PlasmaProjectileEntity)) {
            if (this.tickCount % 50 == 0) {
                this.level.explode(null, this.getX(), this.getY(), this.getZ(), 4.0F, Explosion.BlockInteraction.BREAK);
                this.discard();
            }
            else {
                Vec3 vector3d1 = this.getDeltaMovement();
                double baseYOffset = 0.15D;
                ((ServerLevel) this.level).sendParticles(YELLOW, this.getX() - vector3d1.x, this.getY() - (vector3d1.y + baseYOffset), this.getZ() - vector3d1.z, 1, 0, 0, 0, 0);
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

    @Override @NotNull public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override public boolean isPickable() { return false; }

    @Override @NotNull protected SoundEvent getDefaultHitGroundSoundEvent() { return SoundEvents.NETHER_GOLD_ORE_HIT; }

    @Override protected float getWaterInertia() { return 1F; }

    @Override protected boolean canHitEntity(@NotNull Entity entity) { return super.canHitEntity(entity) && !entity.noPhysics; }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, p -> PlayState.CONTINUE));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
