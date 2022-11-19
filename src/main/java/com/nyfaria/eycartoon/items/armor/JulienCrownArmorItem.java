package com.nyfaria.eycartoon.items.armor;

import com.nyfaria.eycartoon.init.ItemInit;
import com.nyfaria.eycartoon.init.MorphInit;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
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

public class JulienCrownArmorItem extends GeoArmorItem implements IAnimatable {

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public JulienCrownArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    private static final MobEffectInstance JUMP_BOOST = new MobEffectInstance(MobEffects.JUMP, 200, 2, false, false);
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);
        if (!level.isClientSide) {
            player.addEffect(JUMP_BOOST);
            player.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemInit.LEMUR_FEET.get()));
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
