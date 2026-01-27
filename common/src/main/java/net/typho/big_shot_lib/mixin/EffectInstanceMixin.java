package net.typho.big_shot_lib.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.typho.big_shot_lib.shaders.mixins.ShaderLocationsInfo;
import net.typho.big_shot_lib.shaders.mixins.ShaderMixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectInstance.class)
public class EffectInstanceMixin {
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/EffectInstance;getOrCreate(Lnet/minecraft/server/packs/resources/ResourceProvider;Lcom/mojang/blaze3d/shaders/Program$Type;Ljava/lang/String;)Lcom/mojang/blaze3d/shaders/EffectProgram;",
                    ordinal = 0
            )
    )
    private void setThreadLocal(ResourceProvider resourceProvider, String name, CallbackInfo ci) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.set(DefaultVertexFormat.BLIT_SCREEN);
            ShaderMixinManager.currentLocationsInfo.set(ShaderMixinManager.enabled ? new ShaderLocationsInfo(false) : null);
        }
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/shaders/ProgramManager;createProgram()I"
            )
    )
    private void clearThreadLocal(ResourceProvider resourceProvider, String name, CallbackInfo ci) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.remove();
            ShaderMixinManager.currentLocationsInfo.remove();
        }
    }
}
