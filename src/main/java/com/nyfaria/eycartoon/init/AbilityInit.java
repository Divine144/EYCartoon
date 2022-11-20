package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.ability.AirStrikeAbility;
import com.nyfaria.eycartoon.ability.DoubleJumpAbility;
import com.nyfaria.eycartoon.ability.IceAbility;
import com.nyfaria.eycartoon.ability.ToothlessBeamAbility;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediaabilities.init.HMAAbilityInit;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AbilityInit {
    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(HMAAbilityInit.ABILITIES.getRegistryKey(), EYCartoon.MODID);

    public static final RegistryObject<Ability> ICE_ABILITY = ABILITIES.register("ice", IceAbility::new);
    public static final RegistryObject<Ability> DOUBLE_JUMP_ABILITY = ABILITIES.register("double_jump", DoubleJumpAbility::new);
    public static final RegistryObject<Ability> AIR_STRIKE_ABILITY = ABILITIES.register("air_strike", AirStrikeAbility::new);
    public static final RegistryObject<Ability> TOOTHLESS_ABILITY = ABILITIES.register("beam", ToothlessBeamAbility::new);
}
