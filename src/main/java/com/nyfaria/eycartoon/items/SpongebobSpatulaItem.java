package com.nyfaria.eycartoon.items;

import com.nyfaria.eycartoon.effect.FartLevitationEffect;
import com.nyfaria.eycartoon.entity.KrabbyPattyProjectileEntity;
import com.nyfaria.eycartoon.init.ItemInit;
import com.nyfaria.eycartoon.init.MobEffectInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class SpongebobSpatulaItem extends Item {

    public SpongebobSpatulaItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player player) {
            pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                pStack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(player.getUsedItemHand()));
                if (getUseDuration(pStack) - pTimeCharged >= 10) {
                    Snowball patty = new Snowball(pLevel, player) {
                        @Override
                        protected void onHitEntity(@NotNull EntityHitResult pResult) {
                            Entity entity = pResult.getEntity();
                            if (entity instanceof LivingEntity entity1) {
                                entity1.hurt(DamageSource.thrown(this, this.getOwner()), 0.5F);
                                var effect = MobEffectInit.FART_LEVITATION_EFFECT.get();
                                if (effect instanceof FartLevitationEffect effect1) {
                                    effect1.setIsInfinite(true);
                                }
                                entity1.addEffect(new MobEffectInstance(effect, Integer.MAX_VALUE, 0, false, false, false));
                            }
                        }
                    };
                    patty.setItem(new ItemStack(ItemInit.KRABBY_PATTY.get()));
                    patty.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                    pLevel.addFreshEntity(patty);
                }
            }
        }
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
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
