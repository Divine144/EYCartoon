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
    public final ForgeConfigSpec.IntValue minionLaunchedDropOrder;
    public final ForgeConfigSpec.IntValue krabbyPattyDropOrder;
    public final ForgeConfigSpec.IntValue cobbleStoneDropOrder;
    public final ForgeConfigSpec.IntValue shreksFistsDropOrder;
    public final ForgeConfigSpec.IntValue bossBabyDropOrder;
    public final ForgeConfigSpec.IntValue zombieCowDropOrder;
    public final ForgeConfigSpec.IntValue squidwardTraderDropOrder;
    public final ForgeConfigSpec.IntValue pigWolfHorseDropOrder;
    public final ForgeConfigSpec.IntValue cartoonRayDropOrder;
    public final ForgeConfigSpec.IntValue mcqueenMobileDropOrder;
    public final ForgeConfigSpec.IntValue buzzControlPanelDropOrder;
    public final ForgeConfigSpec.IntValue ladybugYoYoDropOrder;
    public final ForgeConfigSpec.IntValue skeletonsDropOrder;
    public final ForgeConfigSpec.IntValue sonicCoinsDropOrder;
    public final ForgeConfigSpec.IntValue sonicBootsDropOrder;
    public final ForgeConfigSpec.IntValue petToothlessDropOrder;
    public final ForgeConfigSpec.IntValue kingJulienCrownDropOrder;
    public final ForgeConfigSpec.IntValue diamondsDropOrder;

    private EYCartoonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("EYCartoon Config");
        enableIceAbilityRedstonePos = createRedstonePosEntry(builder, "Ice Ability", "unlocking the ice ability");
        minionLaunchedDropOrder = builder.defineInRange("Minion Launcher Drop Order", 1, 1, 1000);
        krabbyPattyDropOrder = builder.defineInRange("Krabby Patty Drop Order", 2, 1, 1000);
        cobbleStoneDropOrder = builder.defineInRange("Cobblestone Drop Order", 3, 1, 1000);
        shreksFistsDropOrder = builder.defineInRange("Shrek's Fists Drop Order", 5, 1, 1000);
        bossBabyDropOrder = builder.defineInRange("Boss Baby Drop Order", 11, 1, 1000);
        zombieCowDropOrder = builder.defineInRange("Zombie Cows Drop Order", 12, 1, 1000);
        squidwardTraderDropOrder = builder.defineInRange("Squidward Trader Drop Order", 17, 1, 1000);
        pigWolfHorseDropOrder = builder.defineInRange("Mobs (Pig, Horse, Wolf) Drop Order", 29, 1, 1000);
        cartoonRayDropOrder = builder.defineInRange("Cartoon Ray Drop Order", 30, 1, 1000);
        mcqueenMobileDropOrder = builder.defineInRange("McQueen Mobile Drop Order", 31, 1, 1000);
        buzzControlPanelDropOrder = builder.defineInRange("Buzz's Control Panel Drop Order", 45, 1, 1000);
        ladybugYoYoDropOrder = builder.defineInRange("Ladybug Yoyo Drop Order", 50, 1, 1000);
        skeletonsDropOrder = builder.defineInRange("Skeletons Drop Order", 51, 1, 1000);
        sonicCoinsDropOrder = builder.defineInRange("Sonic Coins Drop Order", 54, 1, 1000);
        sonicBootsDropOrder = builder.defineInRange("Sonic Boots Drop Order", 55, 1, 1000);
        petToothlessDropOrder = builder.defineInRange("Pet Toothless Drop Order", 60, 1, 1000);
        kingJulienCrownDropOrder = builder.defineInRange("King Julien's Crown Drop Order", 70, 1, 1000);
        diamondsDropOrder = builder.defineInRange("Diamonds Drop Order", 73, 1, 1000);
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