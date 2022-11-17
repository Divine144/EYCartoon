package com.nyfaria.eycartoon.init;

import com.google.common.collect.ImmutableSet;
import com.nyfaria.eycartoon.EYCartoon;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ProfessionsInit {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, EYCartoon.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, EYCartoon.MODID);

    public static final RegistryObject<PoiType> SQUIDWARD_POI = POI_TYPES.register("squidward_poi_type",
            () -> new PoiType(ImmutableSet.copyOf(Blocks.BEDROCK.getStateDefinition().getPossibleStates()), 1, 1));

    public static final RegistryObject<VillagerProfession> SQUIDWARD = VILLAGER_PROFESSIONS.register("squidward",
            () -> new VillagerProfession("squidward", x -> x.get() == SQUIDWARD_POI.get(),  x -> x.get() == SQUIDWARD_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_YES));

    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class).invoke(null, SQUIDWARD_POI.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
