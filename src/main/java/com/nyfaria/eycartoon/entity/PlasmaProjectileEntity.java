package com.nyfaria.eycartoon.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PlasmaProjectileEntity extends MinionProjectileEntity {

    // stil gota do fire logic
    public PlasmaProjectileEntity(EntityType<? extends PlasmaProjectileEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount % 200 == 0) {
            this.discard();
        }
    }

    @Override @NotNull
    protected SoundEvent getDefaultHitGroundSoundEvent() { return SoundEvents.GENERIC_EXPLODE; }
}
