package net.typho.big_shot_lib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.ShaderProgram;
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo;
import net.typho.big_shot_lib.spirv.ShaderMixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$CompilationCache")
public class ShaderManagerCompilationCacheMixin {
    @Inject(
            method = "compileProgram",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderManager$CompilationCache;getOrCompileShader(Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/shaders/CompiledShader$Type;Lnet/minecraft/client/renderer/ShaderDefines;)Lcom/mojang/blaze3d/shaders/CompiledShader;",
                    ordinal = 0
            )
    )
    private void setThreadLocal(ShaderProgram program, CallbackInfoReturnable<CompiledShaderProgram> cir) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.set(program.vertexFormat());
            ShaderMixinManager.currentLocationsInfo.set(ShaderMixinManager.enabled ? new ShaderLocationsInfo(program.vertexFormat(), false) : null);
        }
    }

    @Inject(
            method = "compileProgram",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderManager;linkProgram(Lnet/minecraft/client/renderer/ShaderProgram;Lnet/minecraft/client/renderer/ShaderProgramConfig;Lcom/mojang/blaze3d/shaders/CompiledShader;Lcom/mojang/blaze3d/shaders/CompiledShader;)Lnet/minecraft/client/renderer/CompiledShaderProgram;"
            )
    )
    private void clearThreadLocal(ShaderProgram program, CallbackInfoReturnable<CompiledShaderProgram> cir) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.remove();
            ShaderMixinManager.currentLocationsInfo.remove();
        }
    }

    @WrapOperation(
            method = "getOrCompileShader",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private <K, V> V getOrCompileShader(Map<K, V> instance, Object o, Operation<V> original) {
        return null;
    }
}
