package com.nyfaria.eycartoon.client.animatable;

import com.nyfaria.eycartoon.network.NetworkHandler;
import com.nyfaria.eycartoon.network.packets.serverbound.DoHurtPacket;
import dev._100media.hundredmediageckolib.client.animatable.SimpleAnimatable;
import dev._100media.hundredmediamorphs.capability.AnimationHolderAttacher;
import net.minecraft.client.player.AbstractClientPlayer;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class SpinAnimatable extends SimpleAnimatable {

    private AbstractClientPlayer player;

    @Override
    protected <E extends IAnimatable> PlayState animationEvent(AnimationEvent<E> event) {
        AnimationController<E> controller = event.getController();
        player = event.getExtraDataOfType(AbstractClientPlayer.class).get(0);
        var holder = AnimationHolderAttacher.getAnimationHolderUnwrap(player);
        if (holder != null && holder.getCurrentMotionAnimation() != null) {
            controller.setAnimation(holder.getCurrentMotionAnimation().getAnimations(player).toGeckolibBuilder());
        }
        else {
            controller.setAnimation(new AnimationBuilder().addAnimation("spin", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return super.animationEvent(event);
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        data.getAnimationControllers().get("controller").registerCustomInstructionListener(this::customInstructionEvent);
    }

    public void customInstructionEvent(CustomInstructionKeyframeEvent<?> event) {
        if (event.instructions.contains("attackHurt")) {
            NetworkHandler.INSTANCE.sendToServer(new DoHurtPacket(4));
        }
    }
}
