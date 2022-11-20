package com.nyfaria.eycartoon.client.model;

import com.nyfaria.eycartoon.entity.ToothlessEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nullable;

@SuppressWarnings("removal")
public class ToothlessModel extends SimpleGeoEntityModel<ToothlessEntity> {

    public ToothlessModel(String namespace, String name) {
        super(namespace, name);
    }

    @Override
    public void setLivingAnimations(ToothlessEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if(entity.getControllingPassenger() != null) {
            Player player = entity.getControllingPassenger();
            IBone head = this.getAnimationProcessor().getBone("head");
            if (head != null) {
                head.setRotationX(player.getXRot() * ((float) Math.PI / 180F));
            }
        }
    }
}
