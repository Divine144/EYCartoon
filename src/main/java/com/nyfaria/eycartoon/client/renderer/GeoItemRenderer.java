package com.nyfaria.eycartoon.client.renderer;

import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedItemModel;
import dev._100media.hundredmediageckolib.item.animated.IAnimatedItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;

public class GeoItemRenderer<T extends Item & IAnimatable & IAnimatedItem> extends software.bernie.geckolib3.renderers.geo.GeoItemRenderer<T> {
    public GeoItemRenderer() {
        super(new SimpleAnimatedItemModel<>() {
            @Override
            public ResourceLocation getModelResource(T object) {
                return geoCache.computeIfAbsent(getRegistryName(object), k -> new ResourceLocation(k.getNamespace(), "geo/" + k.getPath() + "_item" + ".geo.json"));
            }
            private static ResourceLocation getRegistryName(Item item) {
                return ForgeRegistries.ITEMS.getKey(item);
            }
        });
    }
}
