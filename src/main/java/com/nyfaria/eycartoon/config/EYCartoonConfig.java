package com.nyfaria.eycartoon.config;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.function.Function;

public class EYCartoonConfig {
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final EYCartoonConfig INSTANCE;

    static {
        Pair<EYCartoonConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(EYCartoonConfig::new);
        CONFIG_SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public final Function<Level, GlobalPos> enableIceAbilityRedstonePos;
    private EYCartoonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("EYCartoon Config");
        enableIceAbilityRedstonePos = createRedstonePosEntry(builder, "Ice Ability", "unlocking the ice ability");
        builder.pop();
    }

    private static Function<Level, GlobalPos> createRedstonePosEntry(ForgeConfigSpec.Builder builder, String name, String description) {
        ForgeConfigSpec.ConfigValue<String> posValue = builder
                .comment("Block position in the form \"X,Y,Z\" of the redstone block to check for when " + description)
                .define(name + "RedstonePos", "none", EYCartoonConfig::validateBlockPos);
        ForgeConfigSpec.ConfigValue<String> dimensionValue = builder
                .comment("Dimension of the redstone block to check for when " + description)
                .define(name + "RedstoneDimension", Level.OVERWORLD.location().toString(),  EYCartoonConfig::validateDimension);
        return level -> getRedstonePos(level, posValue, dimensionValue);
    }

    private static boolean validateDimension(Object obj) {
        return obj instanceof String str && ResourceLocation.isValidResourceLocation(str);
    }

    private static boolean validateBlockPos(Object obj) {
        if (!(obj instanceof String str))
            return false;

        if (str.equalsIgnoreCase("none"))
            return true;

        String[] parts = str.split(",");
        if (parts.length != 3)
            return false;

        for (int i = 0; i < parts.length; i++) {
            try {
                Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static GlobalPos getRedstonePos(Level level, ForgeConfigSpec.ConfigValue<String> posValue, ForgeConfigSpec.ConfigValue<String> dimensionValue) {
        String str = posValue.get();
        if (str.equalsIgnoreCase("none"))
            return null;

        ResourceLocation dimLoc = ResourceLocation.tryParse(dimensionValue.get());
        if (dimLoc == null)
            return null;

        ResourceKey<Level> dimKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimLoc);
        if (level.getServer() == null || !level.getServer().levelKeys().contains(dimKey))
            return null;

        String[] parts = str.split(",");
        try {
            return GlobalPos.of(dimKey, new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}