package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import kotlin.collections.CollectionsKt;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderLoaderType;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType;
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier;
import net.typho.big_shot_lib.impl.shaders.mixins.ShaderMixinThreadLocal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

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
        ShaderMixinThreadLocal.push(new ShaderProgramKey(
                ShaderLoaderType.MINECRAFT,
                new ResourceIdentifier(name),
                DefaultVertexFormat.BLIT_SCREEN,
                new HashSet<>(CollectionsKt.listOf(ShaderSourceType.VERTEX, ShaderSourceType.FRAGMENT))
        ));
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/shaders/ProgramManager;createProgram()I"
            )
    )
    private void clearThreadLocal(ResourceProvider resourceProvider, String name, CallbackInfo ci) {
        ShaderMixinThreadLocal.pop();
    }
}
