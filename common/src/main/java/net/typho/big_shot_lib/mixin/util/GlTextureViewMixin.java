package net.typho.big_shot_lib.mixin.util;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.opengl.DirectStateAccess;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.opengl.GlTextureView;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack;
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL;
import net.typho.big_shot_lib.api.client.util.dynamic_buffers.DynamicBuffer;
import net.typho.big_shot_lib.api.util.buffers.BufferUploader;
import net.typho.big_shot_lib.impl.util.DynamicBufferRegistry;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;

@Mixin(GlTextureView.class)
public abstract class GlTextureViewMixin extends GpuTextureView {
    @Shadow
    public abstract @NonNull GlTexture texture();

    public GlTextureViewMixin(GpuTexture texture, int baseMipLevel, int mipLevels) {
        super(texture, baseMipLevel, mipLevels);
    }

    @Inject(
            method = "createFbo",
            at = @At("TAIL")
    )
    private void createFbo(
            DirectStateAccess dsa,
            int id,
            CallbackInfoReturnable<Integer> cir,
            @Local(ordinal = 1) int glId
    ) {
        GlTexture mainFboColor = (GlTexture) Minecraft.getInstance().getMainRenderTarget().getColorTexture();
        RenderTarget itemEntity = null;

        try {
            itemEntity = Minecraft.getInstance().levelRenderer.getItemEntityTarget();
        } catch (NullPointerException ignored) {
        }

        if ((mainFboColor != null && mainFboColor.glId() == texture().glId()) || (itemEntity != null && ((GlTexture) itemEntity.getColorTexture()).glId() == texture().glId())) {
            GlStateStack.framebuffer.push(glId);

            List<Integer> buffers = new LinkedList<>();
            buffers.add(GL_COLOR_ATTACHMENT0);
            int i = GL_COLOR_ATTACHMENT1;

            for (DynamicBuffer<?> buffer : DynamicBufferRegistry.buffers) {
                int point = i++;
                buffers.add(point);
                buffer.resize(getWidth(0), getHeight(0), BufferUploader::uploadNull);
                buffer.attachToFramebuffer(point);
            }

            OpenGL.INSTANCE.drawBuffers(buffers.stream().mapToInt(k -> k).toArray());

            GlStateStack.framebuffer.pop();
        }
    }
}
