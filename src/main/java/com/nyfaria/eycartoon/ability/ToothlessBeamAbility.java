package com.nyfaria.eycartoon.ability;

import com.nyfaria.eycartoon.entity.ToothlessEntity;
import dev._100media.hundredmediaabilities.ability.Ability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class ToothlessBeamAbility extends Ability {

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        super.executePressed(level, player);
        if (player.getVehicle() instanceof ToothlessEntity dragon) {
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