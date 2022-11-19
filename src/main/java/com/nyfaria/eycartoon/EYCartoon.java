package com.nyfaria.eycartoon;

import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import com.nyfaria.eycartoon.config.EYCartoonClientConfig;
import com.nyfaria.eycartoon.config.EYCartoonConfig;
import com.nyfaria.eycartoon.datagen.*;
import com.nyfaria.eycartoon.init.*;
import com.nyfaria.eycartoon.network.NetworkHandler;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(EYCartoon.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EYCartoon {
    public static final String MODID = "eycartoon";
    public static final Logger LOGGER = LogManager.getLogger();

    public EYCartoon() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EYCartoonConfig.CONFIG_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EYCartoonClientConfig.CLIENT_SPEC);

        MorphInit.MORPHS.register(modBus);
        ItemInit.ITEMS.register(modBus);

        EntityInit.ENTITIES.register(modBus);
        BlockInit.BLOCKS.register(modBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modBus);
        MobEffectInit.MOB_EFFECTS.register(modBus);
        AbilityInit.ABILITIES.register(modBus);
        ProfessionsInit.POI_TYPES.register(modBus);
        ProfessionsInit.VILLAGER_PROFESSIONS.register(modBus);
        AnimationInit.ANIMATIONS.register(modBus);

        PlayerHolderAttacher.register();
        GeckoLib.initialize();
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.register();
        event.enqueueWork(ProfessionsInit::registerPOIs);
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();

        generator.addProvider(includeServer, new ModRecipeProvider(generator));
        generator.addProvider(includeServer, new ModLootTableProvider(generator));
        generator.addProvider(includeServer, new ModSoundProvider(generator, existingFileHelper));
        generator.addProvider(includeClient, new ModItemModelProvider(generator, existingFileHelper));
        generator.addProvider(includeClient, new ModBlockStateProvider(generator, existingFileHelper));
        generator.addProvider(includeClient, new ModLangProvider(generator));
    }
}
