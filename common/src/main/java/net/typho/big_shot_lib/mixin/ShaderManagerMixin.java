package net.typho.big_shot_lib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo;
import net.typho.big_shot_lib.spirv.ShaderMixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public class ShaderManagerMixin {
    @Inject(
            method = "preloadForStartup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderManager;preloadShader(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/shaders/CompiledShader$Type;Lnet/minecraft/client/renderer/ShaderDefines;)Lcom/mojang/blaze3d/shaders/CompiledShader;",
                    ordinal = 0
            )
    )
    private void setThreadLocal(
            ResourceProvider resourceProvider,
            ShaderProgram[] programs,
            CallbackInfo ci,
            @Local ShaderProgram program
    ) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.set(program.vertexFormat());
            ShaderMixinManager.currentLocationsInfo.set(ShaderMixinManager.enabled ? new ShaderLocationsInfo(program.vertexFormat(), false) : null);
        }
    }

    @Inject(
            method = "preloadForStartup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderManager;linkProgram(Lnet/minecraft/client/renderer/ShaderProgram;Lnet/minecraft/client/renderer/ShaderProgramConfig;Lcom/mojang/blaze3d/shaders/CompiledShader;Lcom/mojang/blaze3d/shaders/CompiledShader;)Lnet/minecraft/client/renderer/CompiledShaderProgram;"
            )
    )
    private void clearThreadLocal(ResourceProvider resourceProvider, ShaderProgram[] programs, CallbackInfo ci) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.remove();
            ShaderMixinManager.currentLocationsInfo.remove();
        }
    }
}
