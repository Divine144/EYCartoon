package com.nyfaria.eycartoon.ability;

import com.nyfaria.eycartoon.init.AnimationInit;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediamorphs.capability.AnimationHolderAttacher;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class AnimationAbility extends Ability {

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        super.executePressed(level, player);
        AnimationHolderAttacher.getAnimationHolderUnwrap(player).setCurrentMotionAnimation(AnimationInit.SPIN.get(),true,true);
    }
}
