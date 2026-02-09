package net.typho.big_shot_lib.mixin.files;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.typho.big_shot_lib.api.shaders.GlShaderRegistry;
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

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V",
                    ordinal = 0
            )
    )
    private void init(GameConfig gameConfig, CallbackInfo ci) {
        resourceManager.registerReloadListener(GlShaderRegistry.INSTANCE.getReloadListener());
    }
}
