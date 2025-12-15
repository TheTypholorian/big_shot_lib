package net.typho.big_shot_lib.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.typho.big_shot_lib.api.impl.NeoWindow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    Minecraft minecraft;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"
            )
    )
    private void render(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        for (NeoWindow window : NeoWindow.Companion.getAUTO_RENDER()) {
            glfwMakeContextCurrent(window.handle());
            //try (var bound = window.framebuffer().bind()) {
            window.render(deltaTracker);
            //}
        }

        glfwMakeContextCurrent(minecraft.getWindow().getWindow());
    }
}
