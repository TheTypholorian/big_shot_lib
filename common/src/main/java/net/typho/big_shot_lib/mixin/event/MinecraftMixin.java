package net.typho.big_shot_lib.mixin.event;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.api.event.WindowResizeEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Final
    private RenderTarget mainRenderTarget;

    @Inject(
            method = "resizeDisplay",
            at = @At("TAIL")
    )
    private void resizeDisplay(CallbackInfo ci) {
        WindowResizeEvent.Companion.invoke(mainRenderTarget.width, mainRenderTarget.height);
    }
}
