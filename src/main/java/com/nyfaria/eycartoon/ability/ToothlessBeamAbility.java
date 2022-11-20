package com.nyfaria.eycartoon.ability;

import com.nyfaria.eycartoon.entity.PlasmaProjectileEntity;
import com.nyfaria.eycartoon.entity.ToothlessEntity;
import com.nyfaria.eycartoon.init.EntityInit;
import dev._100media.hundredmediaabilities.ability.Ability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class ToothlessBeamAbility extends Ability {

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        super.executePressed(level, player);
        if (player.getVehicle() instanceof ToothlessEntity dragon) {
/*            Vec3 viewVector = dragon.getViewVector(1.0F);
            PlasmaProjectileEntity plasma = new PlasmaProjectileEntity(EntityInit.PLASMA_PROJECTILE.get(), level);
            plasma.setPos(dragon.getX() + viewVector.x, dragon.getY(), dragon.getZ() + viewVector.z);
            plasma.setNoGravity(true);
            plasma.shootFromRotation(dragon, dragon.getXRot(), dragon.getYRot(), 0.0F, 1.5F, 0F);
            plasma.setDeltaMovement(plasma.getDeltaMovement().multiply(2.5, 2.5, 2.5));
            plasma.setOwner(dragon);
            level.addFreshEntity(plasma);*/
            dragon.setWarp(true);
        }
    }

    @Override
    public void executeReleased(ServerLevel level, ServerPlayer player) {
        super.executeReleased(level, player);
        if (player.getVehicle() instanceof ToothlessEntity dragon) {
            dragon.setWarp(false);
        }
    }

    @Override
    public boolean isHiddenAbility() {
        return true;
    }
}