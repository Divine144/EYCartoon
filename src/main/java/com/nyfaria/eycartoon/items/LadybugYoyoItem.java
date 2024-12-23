package com.nyfaria.eycartoon.items;

import com.nyfaria.eycartoon.entity.LadyBugRenderer;
import com.nyfaria.eycartoon.entity.ThrownYoyoEntity;
import dev._100media.hundredmediageckolib.item.animated.AnimatedItemProperties;
import dev._100media.hundredmediageckolib.item.animated.IAnimatedItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public class LadybugYoyoItem extends Item implements IAnimatable, IAnimatedItem {

    protected AnimationFactory factory = GeckoLibUtil.createFactory(this);

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
                thrownyoYo.setOwnerUUID(player.getUUID());
                thrownyoYo.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                if (player.getAbilities().instabuild) {
                    thrownyoYo.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
                pLevel.addFreshEntity(thrownyoYo);
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
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public AnimatedItemProperties getAnimatedToolProperties() {
        return new AnimatedItemProperties().animated().defaultDurability(500);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationEvent));
    }

    private <T extends IAnimatable> PlayState animationEvent(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("spin", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final LadyBugRenderer renderer = new LadyBugRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }
}
