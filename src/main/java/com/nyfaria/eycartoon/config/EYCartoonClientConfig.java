package com.nyfaria.eycartoon.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class EYCartoonClientConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final EYCartoonClientConfig CLIENT;

    static {
        Pair<EYCartoonClientConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(EYCartoonClientConfig::new);
        CLIENT_SPEC = pair.getRight();
        CLIENT = pair.getLeft();
    }

    public final ForgeConfigSpec.BooleanValue example;

    public EYCartoonClientConfig(ForgeConfigSpec.Builder builder) {
        this.example = builder.define("example", true);
    }
}

