package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection;
import net.typho.big_shot_lib.api.util.ImmutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(MultiBufferSource.BufferSource.class)
public class MultiBufferSourceBufferSourceMixin implements ImmutableExtension<List<MultiBufferSourceInjection>> {
    @Unique
    private final List<MultiBufferSourceInjection> big_shot_lib$injections = new ArrayList<>();

    @Override
    public List<MultiBufferSourceInjection> getExtensionValue() {
        return big_shot_lib$injections;
    }

    @ModifyReturnValue(
            method = "getBuffer",
            at = @At("RETURN")
    )
    private VertexConsumer getBuffer(VertexConsumer original, @Local(argsOnly = true) RenderType renderType) {
        List<VertexConsumer> consumers = new ArrayList<>(
                Arrays.asList(
                        big_shot_lib$injections.stream()
                                .map(source -> source.getBuffer(renderType))
                                .filter(Objects::nonNull)
                                .toArray(VertexConsumer[]::new)
                )
        );

        if (consumers.isEmpty()) {
            return original;
        }

        consumers.addFirst(original);

        return VertexMultiConsumer.create(consumers.toArray(VertexConsumer[]::new));
    }

    @Inject(
            method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V",
            at = @At("TAIL")
    )
    private void endBatch(RenderType renderType, BufferBuilder builder, CallbackInfo ci) {
        for (MultiBufferSourceInjection injection : big_shot_lib$injections) {
            injection.endBatch(renderType);
        }
    }
}
