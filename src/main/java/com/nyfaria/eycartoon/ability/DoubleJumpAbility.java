package com.nyfaria.eycartoon.ability;

import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import dev._100media.hundredmediaabilities.ability.Ability;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class DoubleJumpAbility extends Ability {

    @Override
    public boolean isHiddenAbility() {
        return true;
    }

    int pressedCount = 0;

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        if (++pressedCount == 2) {
            PlayerHolderAttacher.getPlayerHolder(player).ifPresent(p -> {
                if (p.isCanDoubleJump() && !player.isOnGround()) {
                    player.hurtMarked = true;
                    player.jumpFromGround();
                    level.sendParticles(ParticleTypes.POOF, player.getX(), player.getY() - 1, player.getZ(), 4, 0, 0, 0, 0.1);
                    p.setCanDoubleJump(false);
                }
            });
            pressedCount = 0;
        }
        super.executePressed(level, player);
    }
}
