package com.nyfaria.eycartoon.ability;

import dev._100media.hundredmediaabilities.ability.Ability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class AirStrikeAbility extends Ability {

    @Override
    public boolean isHiddenAbility() {
        return true;
    }

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        super.executePressed(level, player);
    }
}
