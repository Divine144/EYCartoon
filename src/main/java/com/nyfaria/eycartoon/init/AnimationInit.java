package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.EYCartoon;
import dev._100media.hundredmediamorphs.animation.Animation;
import dev._100media.hundredmediamorphs.animation.AnimationData;
import dev._100media.hundredmediamorphs.init.HMMAnimationInit;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AnimationInit {

    public static final DeferredRegister<Animation> ANIMATIONS = DeferredRegister.create(HMMAnimationInit.ANIMATIONS.getRegistryKey(), EYCartoon.MODID);

    public static final RegistryObject<Animation> SPIN = ANIMATIONS.register("spin", () -> new Animation(new Animation.Properties<>().animation(new AnimationData("spin",20000,true))));
}
