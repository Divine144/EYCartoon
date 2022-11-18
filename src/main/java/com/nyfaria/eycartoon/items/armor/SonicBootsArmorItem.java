package com.nyfaria.eycartoon.items.armor;

import net.minecraft.world.entity.EquipmentSlot;
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

public class SonicBootsArmorItem extends GeoArmorItem implements IAnimatable {

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public SonicBootsArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    private float speedTimer = 1;

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        super.onArmorTick(stack, level, player);
        if (!level.isClientSide) {
            var speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (player.isSprinting()) {
                if (speedTimer < 200) {
                    speedTimer++;
                }
                if (speed != null) {
                    if (speedTimer < 200) {
                        speed.setBaseValue(speed.getBaseValue() + (speedTimer / 20000));
                        System.out.println(speed.getBaseValue());
                    }
                }
            }
            else  {
                speedTimer = 0;
                if (speed != null) {
                    speed.setBaseValue(0.1F);
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
