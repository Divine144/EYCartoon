package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.EYCartoon;
import dev._100media.hundredmediamorphs.init.HMMMorphInit;
import dev._100media.hundredmediamorphs.morph.AdvancedGeoPlayerMorph;
import dev._100media.hundredmediamorphs.morph.Morph;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MorphInit {
    public static final DeferredRegister<Morph> MORPHS = DeferredRegister.create(HMMMorphInit.MORPHS.getRegistryKey(), EYCartoon.MODID);

    public static final RegistryObject<Morph> PLAYER_SPINNING = MORPHS.register("player_spinning", () -> new Morph(new Morph.Properties<>().maxHealth(20)));
}
