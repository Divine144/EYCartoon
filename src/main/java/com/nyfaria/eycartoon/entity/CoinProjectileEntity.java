package com.nyfaria.eycartoon.entity;

import com.nyfaria.eycartoon.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CoinProjectileEntity extends Arrow implements IAnimatable {

    private static final EntityDataAccessor<BlockPos> INITIAL_POS = SynchedEntityData.defineId(CoinProjectileEntity.class, EntityDataSerializers.BLOCK_POS);

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public CoinProjectileEntity(EntityType<? extends Arrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(INITIAL_POS, new BlockPos(0, 0, 0));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("BlockPos")) {
            this.entityData.set(INITIAL_POS, NbtUtils.readBlockPos(pCompound));
        }
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.put("BlockPos", NbtUtils.writeBlockPos(this.entityData.get(INITIAL_POS)));
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void tick() {
        super.tick();
        if (getInitialFacing() != null) {
            switch (getInitialFacing()) {
                case EAST -> this.setDeltaMovement(3, 0, 0);
                case WEST -> this.setDeltaMovement(-3, 0, 0);
                case SOUTH -> this.setDeltaMovement(0, 0, 3);
                case NORTH -> this.setDeltaMovement(0, 0, -3);
                case UP -> this.setDeltaMovement(0, 3, 0);
                case DOWN -> this.setDeltaMovement(0, -3, 0);
            }
            if (Math.sqrt(this.distanceToSqr(asVec3(getInitialPosition()))) > 10.0D) {
                this.discard();
            }
            else if (this.tickCount >= 100) {
                this.discard();
            }
        }
        else {
            this.discard();
        }
    }

    @Nullable
    public Direction getInitialFacing() {
        if (this.getInitialPosition() != null) {
            BlockState state = this.level.getBlockState(new BlockPos(this.getInitialPosition()));
            if (state.is(BlockInit.CASH_REGISTER_BLOCK.get())) {
                return state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            }
        }
        return null;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        entity.hurt(DamageSource.GENERIC, 2F);
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

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    public BlockPos getInitialPosition() {
        return this.entityData.get(INITIAL_POS);
    }

    public void setInitialPosition(BlockPos position) {
        this.entityData.set(INITIAL_POS, position);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, p -> PlayState.CONTINUE));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public Vec3 asVec3(BlockPos pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }
}
