package net.typho.big_shot_lib.mixin.neoforge.client.entrypoint;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforgespi.language.IModInfo;
import net.typho.big_shot_lib.api.event.NeoClientEventBus;
import net.typho.big_shot_lib.client.api.NeoClientInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "thedarkcolour.kotlinforforge.neoforge.KotlinModContainer")
public abstract class KotlinClientModContainerMixin extends ModContainer {
    @Shadow
    private Object modInstance;

    public KotlinClientModContainerMixin(IModInfo info) {
        super(info);
    }

    @Inject(
            method = "constructMod",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;trace(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                    ordinal = 2
            )
    )
    private void constructMod(CallbackInfo ci) {
        if (modInstance instanceof NeoClientInitializer) {
            ((NeoClientInitializer) modInstance).onInitializeClient(NeoClientEventBus.get(getModId()));
        }
    }
}
