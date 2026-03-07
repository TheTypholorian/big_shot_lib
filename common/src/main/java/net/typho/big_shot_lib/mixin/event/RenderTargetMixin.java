package net.typho.big_shot_lib.mixin.event;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.typho.big_shot_lib.impl.state.OpenGLImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderTarget.class)
public class RenderTargetMixin {
    @Inject(
            method = "bindWrite",
            at = @At("TAIL")
    )
    private void bindWrite(boolean setViewport, CallbackInfo ci) {
        OpenGLImpl.currentTarget = (RenderTarget) (Object) this;
    }

    @Inject(
            method = "unbindWrite",
            at = @At("TAIL")
    )
    private void unbindWrite1(CallbackInfo ci) {
        OpenGLImpl.currentTarget = null;
    }
}
