package com.nyfaria.eycartoon.datagen;

import com.nyfaria.eycartoon.EYCartoon;
import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundProvider extends SoundDefinitionsProvider {
    public ModSoundProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, EYCartoon.MODID, helper);
    }

    @Override
    public void registerSounds() {
        // SoundInit.SOUNDS.getEntries().forEach(this::addSound);
    }

    public void addSound(RegistryObject<SoundEvent> entry) {
        add(entry, SoundDefinition.definition().with(sound(entry.getId())));
    }
}
