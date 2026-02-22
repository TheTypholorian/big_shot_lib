package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.opengl.GlRenderPipeline;
import com.mojang.blaze3d.opengl.GlShaderModule;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.ShaderType;
import kotlin.collections.CollectionsKt;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.BigShotLib;
import net.typho.big_shot_lib.api.client.rendering.buffers.AlbedoDynamicBuffer;
import net.typho.big_shot_lib.api.client.rendering.buffers.NormalsDynamicBuffer;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderLoaderType;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType;
import net.typho.big_shot_lib.impl.meshes.NeoVertexFormatImpl;
import net.typho.big_shot_lib.impl.shaders.mixins.ShaderMixinThreadLocal;
import net.typho.big_shot_lib.impl.util.VoidMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Map;
import java.util.function.BiFunction;

@Mixin(GlDevice.class)
public class GlDeviceMixin {
    @Shadow
    @Final
    @Mutable
    private Map<GlDevice.ShaderCompilationKey, GlShaderModule> shaderCache;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(
            long window,
            int debugVerbosity,
            boolean synchronous,
            BiFunction<Identifier, ShaderType, String> defaultShaderSource,
            boolean renderDebugLabels,
            CallbackInfo ci
    ) {
        shaderCache = new VoidMap<>();
    }

    @ModifyArg(
            method = "compileShader",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/opengl/GlStateManager;glShaderSource(ILjava/lang/String;)V",
                    remap = false
            ),
            index = 1
    )
    private static String compile(
            String code,
            @Local(argsOnly = true) GlDevice.ShaderCompilationKey key
    ) {
        return ShaderMixinThreadLocal.INSTANCE.get().apply(
                switch (key.type()) {
                    case VERTEX -> ShaderSourceType.VERTEX;
                    case FRAGMENT -> ShaderSourceType.FRAGMENT;
                },
                code
        );
    }

    @Inject(
            method = "compilePipeline",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/opengl/GlDevice;getOrCompileShader(Lnet/minecraft/resources/Identifier;Lcom/mojang/blaze3d/shaders/ShaderType;Lnet/minecraft/client/renderer/ShaderDefines;Ljava/util/function/BiFunction;)Lcom/mojang/blaze3d/opengl/GlShaderModule;",
                    ordinal = 0
            )
    )
    private void setThreadLocal(
            RenderPipeline pipeline,
            BiFunction<Identifier, ShaderType, String> shaderSource,
            CallbackInfoReturnable<GlRenderPipeline> cir
    ) {
        ShaderMixinThreadLocal.push(new ShaderProgramKey(
                ShaderLoaderType.MINECRAFT,
                BigShotLib.toNeo(pipeline.getLocation()),
                new NeoVertexFormatImpl(pipeline.getVertexFormat()),
                new HashSet<>(CollectionsKt.listOf(ShaderSourceType.VERTEX, ShaderSourceType.FRAGMENT)),
                pipeline == RenderPipelines.END_PORTAL ? new HashSet<>(CollectionsKt.listOf(NormalsDynamicBuffer.INSTANCE.location(), AlbedoDynamicBuffer.INSTANCE.location())) : new HashSet<>(),
                pipeline.getVertexFormatMode().primitiveLength <= 2 || pipeline == RenderPipelines.OPAQUE_PARTICLE || pipeline == RenderPipelines.TRANSLUCENT_PARTICLE ? new HashSet<>(CollectionsKt.listOf(NormalsDynamicBuffer.INSTANCE.location(), AlbedoDynamicBuffer.INSTANCE.location())) : new HashSet<>()
        ));
    }

    @Inject(
            method = "compilePipeline",
            at = @At("RETURN")
    )
    private void clearThreadLocal(RenderPipeline pipeline, BiFunction<Identifier, ShaderType, String> shaderSource, CallbackInfoReturnable<GlRenderPipeline> cir) {
        ShaderMixinThreadLocal.pop();
    }
}
