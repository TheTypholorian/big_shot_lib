package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection;
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderSettings;
import net.typho.big_shot_lib.impl.client.rendering.util.NeoRenderSettingsImpl;
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

        return new VertexConsumer() {
            @Override
            public @NotNull VertexConsumer addVertex(float v, float v1, float v2) {
                consumers.forEach(it -> it.vertex(v, v1, v2));
                return original.addVertex(v, v1, v2);
            }

            @Override
            public @NotNull VertexConsumer setColor(int i, int i1, int i2, int i3) {
                consumers.forEach(it -> it.color(i, i1, i2, i3));
                return original.setColor(i, i1, i2, i3);
            }

            @Override
            public @NotNull VertexConsumer setUv(float v, float v1) {
                consumers.forEach(it -> it.textureUV(v, v1));
                return original.setUv(v, v1);
            }

            @Override
            public @NotNull VertexConsumer setUv1(int i, int i1) {
                consumers.forEach(it -> it.overlayUV(i, i1));
                return original.setUv1(i, i1);
            }

            @Override
            public @NotNull VertexConsumer setUv2(int i, int i1) {
                consumers.forEach(it -> it.lightUV(i, i1));
                return original.setUv2(i, i1);
            }

            @Override
            public @NotNull VertexConsumer setNormal(float v, float v1, float v2) {
                consumers.forEach(it -> it.normal(v, v1, v2));
                return original.setNormal(v, v1, v2);
            }
        };
    }

    @Inject(
            method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V",
            at = @At("TAIL")
    )
    private void endBatch(RenderType p_350903_, BufferBuilder p_350797_, CallbackInfo ci) {
        NeoRenderSettings settings = WrapperUtilImplKt.getNeo(p_350903_);

        for (MultiBufferSourceInjection injection : big_shot_lib$injections) {
            injection.endBatch(settings);
        }
    }
}
