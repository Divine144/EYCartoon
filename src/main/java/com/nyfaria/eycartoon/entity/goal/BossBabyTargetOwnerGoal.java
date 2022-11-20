package com.nyfaria.eycartoon.entity.goal;

import com.nyfaria.eycartoon.entity.BossBabyEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BossBabyTargetOwnerGoal extends TargetGoal {

    private final BossBabyEntity entity;

    public BossBabyTargetOwnerGoal(BossBabyEntity pMob, boolean pMustSee) {
        super(pMob, pMustSee);
        this.entity = pMob;
    }

    @Override
    public boolean canUse() {
        if (entity.getOwner() != null) {
            this.findTarget();
            return entity.getOwner() == null;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return entity.getOwner() == null;
    }

    @Override
    protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
        return false;
    }

    protected void findTarget() {
        Player target = this.mob.level.getNearestPlayer(TargetingConditions.forNonCombat().range(20).selector(p -> p instanceof Player), this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        if (target != null) {
            entity.setOwnerUUID(target.getUUID());
        }
    }
}
