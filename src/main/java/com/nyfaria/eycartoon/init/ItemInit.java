package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.items.CartoonRayItem;
import com.nyfaria.eycartoon.items.LadybugYoyoItem;
import com.nyfaria.eycartoon.items.ShreksFistItem;
import com.nyfaria.eycartoon.items.SpongebobSpatulaItem;
import dev._100media.hundredmediageckolib.item.animated.AnimatedItemProperties;
import dev._100media.hundredmediageckolib.item.animated.SimpleAnimatedItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EYCartoon.MODID);

    public static final RegistryObject<Item> KRABBY_PATTY = ITEMS.register("krabby_patty",
            () -> new SimpleAnimatedItem(new AnimatedItemProperties()
            .food(new FoodProperties.Builder()
                    .alwaysEat().nutrition(2).saturationMod(0.2F).meat()
                    .effect(() -> new MobEffectInstance(MobEffectInit.FART_LEVITATION_EFFECT.get(), 200, 1, false, false), 1F).build())
            .tab(CreativeModeTab.TAB_FOOD)));

    public static final RegistryObject<Item> SONIC_COIN = ITEMS.register("sonic_coin",
            () -> new SimpleAnimatedItem(new AnimatedItemProperties()
                    .food(new FoodProperties.Builder().alwaysEat().nutrition(0).build()).tab(CreativeModeTab.TAB_FOOD)));

    public static final RegistryObject<Item> LADYBUG_YOYO = ITEMS.register("ladybug_yoyo", () -> new LadybugYoyoItem(new Item.Properties().durability(500).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> SPONGEBOB_SPATULA = ITEMS.register("spongebobs_spatula", () -> new SpongebobSpatulaItem(new Item.Properties().durability(500).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BUZZ_CONTROL_PANEL = ITEMS.register("buzz_control_panel", () -> new SimpleAnimatedItem(new AnimatedItemProperties().stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> SHREKS_FIST = ITEMS.register("shreks_fist", () -> new ShreksFistItem(new Item.Properties().durability(1000).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> CARTOON_RAY = ITEMS.register("2d_cartoon_ray", () -> new CartoonRayItem(new Item.Properties().durability(500).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> SPONGEBOBS_TIE = ITEMS.register("spongebobs_tie", () -> new SimpleAnimatedItem(new AnimatedItemProperties().stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> PATRICKS_UNDERWEAR = ITEMS.register("patricks_underwear", () -> new SimpleAnimatedItem(new AnimatedItemProperties().stacksTo(1).tab(CreativeModeTab.TAB_MISC)));


}
