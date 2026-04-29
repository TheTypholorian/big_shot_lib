package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection;
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderSettings;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer;
import net.typho.big_shot_lib.api.util.WrapperUtil;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.WrapperUtilImplKt;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//? if <1.21.11 {
import net.minecraft.client.renderer.RenderType;
//? } else {
/*import net.minecraft.client.renderer.rendertype.RenderType;
*///? }

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(MultiBufferSource.BufferSource.class)
public class MultiBufferSourceBufferSourceMixin implements ImmutableExtension<List<MultiBufferSourceInjection>> {
    @Unique
    private final List<MultiBufferSourceInjection> big_shot_lib$injections = new ArrayList<>();

    @Override
    public List<MultiBufferSourceInjection> getBig_shot_lib$extension_value() {
        return big_shot_lib$injections;
    }

    @ModifyReturnValue(
            method = "getBuffer",
            at = @At("RETURN")
    )
    private VertexConsumer getBuffer(VertexConsumer original, @Local(argsOnly = true) RenderType renderType) {
        NeoRenderSettings settings = WrapperUtilImplKt.getNeo(renderType);
        var consumers = big_shot_lib$injections.stream()
                .map(source -> source.getBuffer(settings))
                .filter(Objects::nonNull)
                .toList();
        NeoVertexConsumer neoOriginal = WrapperUtil.Companion.getINSTANCE().wrap(original);

        return WrapperUtil.Companion.getINSTANCE().unwrap(new NeoVertexConsumer() {
            @Override
            public @NotNull NeoVertexConsumer vertex(float x, float y, float z) {
                consumers.forEach(it -> it.vertex(x, y, z));
                neoOriginal.vertex(x, y, z);
                return this;
            }

            @Override
            public @NotNull NeoVertexConsumer color(int r, int g, int b, int a) {
                consumers.forEach(it -> it.color(r, g, b, a));
                neoOriginal.color(r, g, b, a);
                return this;
            }

            @Override
            public @NotNull NeoVertexConsumer textureUV(float u, float v) {
                consumers.forEach(it -> it.textureUV(u, v));
                neoOriginal.textureUV(u, v);
                return this;
            }

            @Override
            public @NotNull NeoVertexConsumer overlayUV(int u, int v) {
                consumers.forEach(it -> it.overlayUV(u, v));
                neoOriginal.overlayUV(u, v);
                return this;
            }

            @Override
            public @NotNull NeoVertexConsumer lightUV(int u, int v) {
                consumers.forEach(it -> it.lightUV(u, v));
                neoOriginal.lightUV(u, v);
                return this;
            }

            @Override
            public @NotNull NeoVertexConsumer normal(float x, float y, float z) {
                consumers.forEach(it -> it.normal(x, y, z));
                neoOriginal.normal(x, y, z);
                return this;
            }

            //? if <1.21 {
            @Override
            public void _endVertex$big_shot_lib() {
                consumers.forEach(it -> it.endVertex());
                neoOriginal.endVertex();
            }
            //? }
        });
    }

    @Inject(
            //? if <1.21 {
            method = "endBatch(Lnet/minecraft/client/renderer/RenderType;)V",
            //? } else if <1.21.11 {
            /*method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V",
            *///? } else {
            /*method = "endBatch(Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V",
            *///? }
            at = @At("TAIL")
    )
    private void endBatch(
            CallbackInfo ci,
            @Local(argsOnly = true) RenderType renderType
    ) {
        NeoRenderSettings settings = WrapperUtilImplKt.getNeo(renderType);

        for (MultiBufferSourceInjection injection : big_shot_lib$injections) {
            injection.endBatch(settings);
        }
    }
}
