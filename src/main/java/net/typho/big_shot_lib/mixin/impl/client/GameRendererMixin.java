package net.typho.big_shot_lib.mixin.impl.client;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.typho.eye_spy.EyeSpy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    public abstract void loadEffect(Identifier resourceLocation);

    @Inject(
            method = "checkEntityPostEffect",
            at = @At("TAIL")
    )
    private void checkEntityPostEffect(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {
            var data = living.getUseItem().get(EyeSpy.spyglassDataComponent.get());

            if (data != null) {
                if (data.lens.is(EyeSpy.creeperLens.get())) {
                    loadEffect(Identifier.minecraft("shaders/post/creeper.json"));
                } else if (data.lens.is(EyeSpy.endermanLens.get())) {
                    loadEffect(Identifier.minecraft("shaders/post/invert.json"));
                }
            }
        }
    }
}
