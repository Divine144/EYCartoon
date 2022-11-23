package com.nyfaria.eycartoon.items;

import com.nyfaria.eycartoon.entity.MinionProjectileEntity;
import com.nyfaria.eycartoon.init.EntityInit;
import com.nyfaria.eycartoon.init.SoundInit;
import dev._100media.hundredmediageckolib.item.animated.AnimatedItemProperties;
import dev._100media.hundredmediageckolib.item.animated.SimpleAnimatedItem;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.network.ISyncable;

import java.util.List;

public class MinionLauncherItem extends SimpleAnimatedItem {

    public MinionLauncherItem(AnimatedItemProperties pProperties) {
        super(pProperties);
    }

    private static final List<SoundEvent> SOUNDS = List.of(SoundInit.FART_ONE.get(), SoundInit.FART_TWO.get(), SoundInit.FART_THREE.get());

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            ItemStack pStack = player.getItemInHand(pUsedHand);
            pStack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(player.getUsedItemHand()));
            MinionProjectileEntity minion = new MinionProjectileEntity(EntityInit.MINION_PROJECTILE.get(), pLevel);
            Vec3 viewVector = player.getViewVector(1.0F);
            minion.setPos(player.getX() + viewVector.x, player.getY() + 1, player.getZ() + viewVector.z);
            minion.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            minion.setOwner(player);
            pLevel.playSound(null, player.blockPosition(), Util.getRandom(SOUNDS, player.getRandom()), SoundSource.PLAYERS, 1F, 1.0F);
            pLevel.addFreshEntity(minion);
        }
        return super.use(pLevel, player, pUsedHand);
    }
}
