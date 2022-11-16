package com.nyfaria.eycartoon.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;

public class ShreksFistItem extends PickaxeItem {

    public ShreksFistItem(Properties pProperties) {
        super(Tiers.NETHERITE, 2, 2, pProperties);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return 1_000_000;
    }
}
