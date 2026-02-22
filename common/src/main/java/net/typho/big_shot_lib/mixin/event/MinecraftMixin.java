package net.typho.big_shot_lib.mixin.event;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.typho.big_shot_lib.BigShotClientEventStorage;
import org.jetbrains.annotations.Nullable;
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

    @Shadow
    @Nullable
    public ClientLevel level;

    @Inject(
            method = "resizeDisplay",
            at = @At("TAIL")
    )
    private void resizeDisplay(CallbackInfo ci) {
        BigShotClientEventStorage.onWindowResized.forEach(event -> event.invoke(mainRenderTarget.width, mainRenderTarget.height));
    }

    @Inject(
            method = "setLevel",
            at = @At("HEAD")
    )
    private void setLevel(ClientLevel level, ReceivingLevelScreen.Reason reason, CallbackInfo ci) {
        BigShotClientEventStorage.onLevelChanged.forEach(event -> event.invoke(this.level, level));
    }
}
