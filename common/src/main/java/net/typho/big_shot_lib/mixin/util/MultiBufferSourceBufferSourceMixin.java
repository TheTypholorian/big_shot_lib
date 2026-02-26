package net.typho.big_shot_lib.mixin.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.MeshData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL;
import net.typho.big_shot_lib.impl.util.DynamicBufferRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;

@Mixin(MultiBufferSource.BufferSource.class)
public class MultiBufferSourceBufferSourceMixin {
    @WrapOperation(
            method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderType;draw(Lcom/mojang/blaze3d/vertex/MeshData;)V"
            )
    )
    private static void renderShadowHead(
            RenderType instance,
            MeshData meshData,
            Operation<Void> original
    ) {
        if (instance == RenderType.entityShadow(ResourceLocation.withDefaultNamespace("textures/misc/shadow.png"))) {
            OpenGL.INSTANCE.drawBuffers(GL_COLOR_ATTACHMENT0);
            original.call(instance, meshData);
            OpenGL.INSTANCE.drawBuffers(IntStream.range(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1 + DynamicBufferRegistry.buffers.size()).toArray());
        } else {
            original.call(instance, meshData);
        }
    }
}
