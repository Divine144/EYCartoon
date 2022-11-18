package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.entity.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.nyfaria.eycartoon.EYCartoon.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    private static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final RegistryObject<EntityType<ThrownYoyoEntity>> THROWN_YOYO_ENTITY = registerEntity("thrown_yoyo", () -> EntityType.Builder.<ThrownYoyoEntity>of(ThrownYoyoEntity::new, MobCategory.MISC).sized(0.5F, 0.5F));
    public static final RegistryObject<EntityType<KrabbyPattyProjectileEntity>> KRABBY_PATTY_PROJECTILE = registerEntity("krabby_projectile", () -> EntityType.Builder.<KrabbyPattyProjectileEntity>of(KrabbyPattyProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryObject<EntityType<RocketEntity>> ROCKET_ENTITY = registerEntity("rocket", () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC).sized(0.7F, 0.7F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryObject<EntityType<CoinProjectileEntity>> COIN_PROJECTILE = registerEntity("coin", () -> EntityType.Builder.of(CoinProjectileEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryObject<EntityType<FlatHorseEntity>> FLAT_HORSE_ENTITY = registerEntity("flat_horse", () -> EntityType.Builder.of(FlatHorseEntity::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).clientTrackingRange(10), Horse::createBaseHorseAttributes);
    public static final RegistryObject<EntityType<FlatPigEntity>> FLAT_PIG_ENTITY = registerEntity("flat_pig", () -> EntityType.Builder.of(FlatPigEntity::new, MobCategory.CREATURE).sized(0.9F, 0.9F).clientTrackingRange(10), Pig::createAttributes);
    public static final RegistryObject<EntityType<FlatWolfEntity>> FLAT_WOLF_ENTITY = registerEntity("flat_wolf", () -> EntityType.Builder.of(FlatWolfEntity::new, MobCategory.CREATURE).sized(0.6F, 0.85F).clientTrackingRange(10), Wolf::createAttributes);
    public static final RegistryObject<EntityType<SquidwardEntity>> SQUIDWARD_TRADER_ENTITY = registerEntity("squidward", () -> EntityType.Builder.of(SquidwardEntity::new, MobCategory.AMBIENT).sized(0.6F, 1.95F).clientTrackingRange(10), Villager::createAttributes);
    public static final RegistryObject<EntityType<BossBabyEntity>> BOSS_BABY = registerEntity("boss_baby", () -> EntityType.Builder.<BossBabyEntity>of(BossBabyEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10), BossBabyEntity::createAttributes);
    public static final RegistryObject<EntityType<ElsaEntity>> ELSA_ENTITY = registerEntity("elsa", () -> EntityType.Builder.of(ElsaEntity::new, MobCategory.MISC).sized(0.6F, 1.95F).clientTrackingRange(10), Mob::createMobAttributes);
    public static final RegistryObject<EntityType<SpongebobEntity>> SPONGEBOB = registerEntity("spongebob", () -> EntityType.Builder.of(SpongebobEntity::new, MobCategory.MISC).clientTrackingRange(10), Mob::createMobAttributes);
    public static final RegistryObject<EntityType<LargeSnowball>> LARGE_SNOWBALL = registerEntity("large_snowball", () -> EntityType.Builder.of(LargeSnowball::new, MobCategory.MISC).sized(0.75F, 0.75F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryObject<EntityType<MegaSnowGolemEntity>> MEGA_SNOW_GOLEM = registerEntity("mega_snow_golem", () -> EntityType.Builder.of(MegaSnowGolemEntity::new, MobCategory.CREATURE).sized(2.0F, 3.0F).clientTrackingRange(15), MegaSnowGolemEntity::createAttributes);
    public static final RegistryObject<EntityType<LightningMcQueenEntity>> LIGHTNING_MCQUEEN = registerEntity("lightning_mcqueen", () -> EntityType.Builder.of(LightningMcQueenEntity::new, MobCategory.MISC).sized(2.5F, 1.5F).clientTrackingRange(10), LightningMcQueenEntity::createAttributes);
    public static final RegistryObject<EntityType<ToothlessEntity>> TOOTHLESS_ENTITY = registerEntity("toothless", () -> EntityType.Builder.of(ToothlessEntity::new, MobCategory.CREATURE).sized(4.0f, 3.0f).clientTrackingRange(10), ToothlessEntity::createAttributes);

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(MODID + ":" + name));
    }

    private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier,
            Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    @SubscribeEvent
    public static void attribs(EntityAttributeCreationEvent e) {
        attributeSuppliers.forEach(p -> e.put(p.entityTypeSupplier.get(), p.factory.get().build()));
    }

    private record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier, Supplier<AttributeSupplier.Builder> factory) {}
}
