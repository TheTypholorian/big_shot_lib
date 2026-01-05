package net.typho.big_shot_lib.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.typho.big_shot_lib.api.impl.NeoFramebuffer;
import net.typho.big_shot_lib.resource.NeoShaderLoader;
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
    private ReloadableResourceManager resourceManager;

    @Shadow
    @Final
    private Window window;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V",
                    ordinal = 0
            )
    )
    private void init(GameConfig gameConfig, CallbackInfo ci) {
        resourceManager.registerReloadListener(NeoShaderLoader.INSTANCE);
    }

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
