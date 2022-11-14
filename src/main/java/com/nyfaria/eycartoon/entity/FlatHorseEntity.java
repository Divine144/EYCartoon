package com.nyfaria.eycartoon.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.Level;

public class FlatHorseEntity extends Horse {
    // Just gotta change the model ig
    public FlatHorseEntity(EntityType<? extends Horse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
