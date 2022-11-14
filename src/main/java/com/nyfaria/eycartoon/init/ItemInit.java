package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.EYCartoon;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EYCartoon.MODID);
}
