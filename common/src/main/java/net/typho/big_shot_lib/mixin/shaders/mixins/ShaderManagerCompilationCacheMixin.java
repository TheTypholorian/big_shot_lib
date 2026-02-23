package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.mojang.blaze3d.shaders.CompiledShader;
import kotlin.collections.CollectionsKt;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.ShaderProgram;
import net.typho.big_shot_lib.BigShotLib;
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderLoaderType;
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey;
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceType;
import net.typho.big_shot_lib.impl.meshes.NeoVertexFormatImpl;
import net.typho.big_shot_lib.impl.shaders.mixins.ShaderMixinThreadLocal;
import net.typho.big_shot_lib.impl.util.VoidMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Map;

@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$CompilationCache")
public class ShaderManagerCompilationCacheMixin {
    @Shadow
    @Final
    @Mutable
    Map<ShaderManager.ShaderCompilationKey, CompiledShader> shaders;

    @Inject(
            method = "compileProgram",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderManager$CompilationCache;getOrCompileShader(Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/shaders/CompiledShader$Type;Lnet/minecraft/client/renderer/ShaderDefines;)Lcom/mojang/blaze3d/shaders/CompiledShader;",
                    ordinal = 0
            )
    )
    private void setThreadLocal(ShaderProgram program, CallbackInfoReturnable<CompiledShaderProgram> cir) {
        ShaderMixinThreadLocal.push(new ShaderProgramKey(
                ShaderLoaderType.MINECRAFT,
                BigShotLib.toNeo(program.configId()),
                new NeoVertexFormatImpl(program.vertexFormat()),
                new HashSet<>(CollectionsKt.listOf(ShaderSourceType.VERTEX, ShaderSourceType.FRAGMENT)),
                new HashSet<>(),
                new HashSet<>()
        ));
    }

    @Inject(
            method = "compileProgram",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderManager;linkProgram(Lnet/minecraft/client/renderer/ShaderProgram;Lnet/minecraft/client/renderer/ShaderProgramConfig;Lcom/mojang/blaze3d/shaders/CompiledShader;Lcom/mojang/blaze3d/shaders/CompiledShader;)Lnet/minecraft/client/renderer/CompiledShaderProgram;"
            )
    )
    private void clearThreadLocal(ShaderProgram program, CallbackInfoReturnable<CompiledShaderProgram> cir) {
        ShaderMixinThreadLocal.pop();
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(ShaderManager shaderManager, ShaderManager.Configs configs, CallbackInfo ci) {
        shaders = new VoidMap<>();
    }
}
