package com.nyfaria.eycartoon.entity;

import com.nyfaria.eycartoon.init.EntityInit;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.LeashKnotRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ThrownYoyoEntity extends AbstractArrow {

    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownYoyoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> THROWN_ITEM = SynchedEntityData.defineId(ThrownYoyoEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(ThrownYoyoEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;

    public ThrownYoyoEntity(EntityType<? extends ThrownYoyoEntity> type, Level level) {
        super(type, level);
    }

    public ThrownYoyoEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(EntityInit.THROWN_YOYO_ENTITY.get(), owner, level);
        this.setOwner(owner);
        entityData.set(THROWN_ITEM, stack.copy());
        entityData.set(ID_FOIL, stack.hasFoil());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ID_FOIL, false);
        entityData.define(THROWN_ITEM, ItemStack.EMPTY);
        entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    public void tick() {
        dealtDamage = (this.inGroundTime > 4) || (this.getOwner() != null && this.distanceTo(this.getOwner()) >= 10.0F);
        Entity entity = getOwner();
        if ((dealtDamage || isNoPhysics()) && entity != null) {
            if (!isAcceptibleReturnOwner()) {
                if (!level.isClientSide && pickup == Pickup.ALLOWED) {
                    spawnAtLocation(getPickupItem(), 0.1F);
                }
                discard();
            }
            else {
                setNoPhysics(true);
                Vec3 vector3d = new Vec3(entity.getX() - getX(), entity.getEyeY() - getY(), entity.getZ() - getZ());
                setPosRaw(getX(), getY() + vector3d.y * 0.045D, getZ());
                if (level.isClientSide) {
                    yOld = getY();
                }
                setDeltaMovement(getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(0.15D)));
                ++clientSideReturnTridentTickCount;
            }
        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        }
        else {
            return false;
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
    }

    @Override
    public ItemStack getPickupItem() {
        return entityData.get(THROWN_ITEM).copy();
    }

    public boolean isFoil() {
        return entityData.get(ID_FOIL);
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity target = pResult.getEntity();
        ItemStack stack = entityData.get(THROWN_ITEM);
        Collection<AttributeModifier> mods = stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND, stack).get(Attributes.ATTACK_DAMAGE);
        float f = 1;
        for (AttributeModifier mod : mods) {
            f += mod.getAmount();
        }
        if (target instanceof LivingEntity livingentity) {
            f += (EnchantmentHelper.getDamageBonus(stack, livingentity.getMobType()));
        }
        Entity owner = getOwner();
        DamageSource damagesource = DamageSource.trident(this, (owner == null ? this : owner));
        dealtDamage = true;
        if (target.hurt(damagesource, f)) {
            if (target.getType() == EntityType.ENDERMAN) {
                return;
            }
            target.setSecondsOnFire(3);
            if (target instanceof LivingEntity livingTarget) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingTarget, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingTarget);
                }

                doPostHurtEffects(livingTarget);
            }
        }
        setDeltaMovement(getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player pEntity) {
        Entity entity = getOwner();
        if (entity == null || entity.getUUID() == pEntity.getUUID()) {
            super.playerTouch(pEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("thrownItem", 10)) {
            entityData.set(THROWN_ITEM, ItemStack.of(pCompound.getCompound("thrownItem")));
        }
        if (pCompound.contains("Owner")) {
            this.entityData.set(DATA_OWNERUUID_ID, Optional.of(pCompound.getUUID("Owner")));
        }
        dealtDamage = pCompound.getBoolean("dealtDamage");
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.put("thrownItem", entityData.get(THROWN_ITEM).save(new CompoundTag()));
        pCompound.putBoolean("dealtDamage", dealtDamage);
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
        super.addAdditionalSaveData(pCompound);
    }

    @Nullable
    public Player getPlayerOwner() {
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

    @Override
    public void tickDespawn() {
        if (pickup != Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
