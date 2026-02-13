package net.typho.big_shot_lib.mixin.neoforge;

import com.mojang.blaze3d.vertex.VertexFormat;
import kotlin.collections.CollectionsKt;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.typho.big_shot_lib.BigShotLib;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderLoaderType;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType;
import net.typho.big_shot_lib.impl.shaders.mixins.ShaderMixinThreadLocal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/VertexFormat;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderInstance;getOrCreate(Lnet/minecraft/server/packs/resources/ResourceProvider;Lcom/mojang/blaze3d/shaders/Program$Type;Ljava/lang/String;)Lcom/mojang/blaze3d/shaders/Program;",
                    ordinal = 0
            )
    )
    private void setThreadLocal(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_, CallbackInfo ci) {
        ShaderMixinThreadLocal.push(new ShaderProgramKey(
                ShaderLoaderType.MINECRAFT,
                BigShotLib.toNeo(shaderLocation),
                p_173338_,
                new HashSet<>(CollectionsKt.listOf(ShaderSourceType.VERTEX, ShaderSourceType.FRAGMENT))
        ));
    }

    @Inject(
            method = "<init>(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/VertexFormat;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/shaders/ProgramManager;createProgram()I"
            )
    )
    private void clearThreadLocal(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_, CallbackInfo ci) {
        ShaderMixinThreadLocal.pop();
    }
}
