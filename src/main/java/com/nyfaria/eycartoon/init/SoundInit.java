package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.EYCartoon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EYCartoon.MODID);

    public static final RegistryObject<SoundEvent> FART_ONE = registerSound("fart_one");
    public static final RegistryObject<SoundEvent> FART_TWO = registerSound("fart_two");
    public static final RegistryObject<SoundEvent> FART_THREE = registerSound("fart_three");
    public static final RegistryObject<SoundEvent> KRABBY_PATTY_FART = registerSound("krabby_patty_fart");

    protected static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(EYCartoon.MODID, name)));
    }
}
