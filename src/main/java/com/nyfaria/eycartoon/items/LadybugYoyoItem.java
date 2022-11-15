package com.nyfaria.eycartoon.items;

import com.nyfaria.eycartoon.entity.ThrownYoyoEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class LadybugYoyoItem extends Item {

    public LadybugYoyoItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        Player player = (Player)pEntityLiving;
        if (!pLevel.isClientSide) {
            if (getUseDuration(pStack) - pTimeLeft >= 10) {
                pStack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(pEntityLiving.getUsedItemHand()));
                ThrownYoyoEntity thrownyoYo = new ThrownYoyoEntity(pLevel, player, pStack);
                thrownyoYo.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                if (player.getAbilities().instabuild) {
                    thrownyoYo.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
                pLevel.addFreshEntity(thrownyoYo);
                pLevel.playSound(null, thrownyoYo, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    player.getInventory().removeItem(pStack);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }
}
