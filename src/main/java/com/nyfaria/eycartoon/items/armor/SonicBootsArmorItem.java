package com.nyfaria.eycartoon.items.armor;

import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import com.nyfaria.eycartoon.init.AnimationInit;
import com.nyfaria.eycartoon.init.MorphInit;
import dev._100media.hundredmediamorphs.capability.AnimationHolderAttacher;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class SonicBootsArmorItem extends GeoArmorItem implements IAnimatable {

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public SonicBootsArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    private float speedTimer = 0;
    private final int knockBackPower = 3;

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);
        if (!level.isClientSide) {
            var speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            var playerCapHolder = PlayerHolderAttacher.getPlayerHolderUnwrap(player);
            var morphHolder = MorphHolderAttacher.getMorphHolderUnwrap(player);
            var animationHolder = AnimationHolderAttacher.getAnimationHolderUnwrap(player);
            if (playerCapHolder != null & morphHolder != null && animationHolder != null && speed != null) {
                if (player.isSprinting()) {
                    if (speedTimer == 0) {
                        playerCapHolder.setBaseSpeed((float) speed.getBaseValue());
                    }
                    if (morphHolder.getCurrentMorph() != MorphInit.PLAYER_SPINNING.get()) {
                        morphHolder.setCurrentMorph(MorphInit.PLAYER_SPINNING.get(), true);
                        animationHolder.setCurrentMotionAnimation(AnimationInit.SPIN.get(), true, true);
                    }
                    if (speedTimer++ < 100) {
                        speed.setBaseValue(speed.getBaseValue() + (speedTimer / 50000));
                    }
                    List<Entity> list = level.getEntities(player, player.getBoundingBox(), EntitySelector.pushableBy(player));
                    if (!list.isEmpty()) {
                        for (Entity e : list) {
                            if (e instanceof LivingEntity l) {
                                l.setLastHurtByPlayer(player);
                                l.knockback((float) knockBackPower * 0.5F, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float)Math.PI / 180F)));
                                l.hurt(DamageSource.GENERIC, 5F);
                            }
                         }
                    }
                }
                else {
                    speedTimer = 0;
                    speed.setBaseValue(playerCapHolder.getBaseSpeed() <= 0.0F ? 0.1F : playerCapHolder.getBaseSpeed());
                    morphHolder.setCurrentMorph(null, true);
                    animationHolder.setCurrentMotionAnimation(null, true, true);
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, p -> PlayState.CONTINUE));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
