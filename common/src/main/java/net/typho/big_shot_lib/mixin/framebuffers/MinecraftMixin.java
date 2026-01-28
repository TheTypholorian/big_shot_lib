package net.typho.big_shot_lib.mixin.framebuffers;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.framebuffers.NeoFramebuffer;
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
    private Window window;

    @Inject(
            method = "resizeDisplay",
            at = @At("TAIL")
    )
    private void resizeDisplay(CallbackInfo ci) {
        for (NeoFramebuffer fbo : NeoFramebuffer.AUTO_RESIZE) {
            fbo.resize(window.getWidth(), window.getHeight());
        }
    }
}
