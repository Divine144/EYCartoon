package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.effect.FartLevitationEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectInit {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EYCartoon.MODID);

    public static final RegistryObject<MobEffect> FART_LEVITATION_EFFECT = MOB_EFFECTS.register("fart_levitation", () -> new FartLevitationEffect(MobEffectCategory.HARMFUL, 3124687));

}
