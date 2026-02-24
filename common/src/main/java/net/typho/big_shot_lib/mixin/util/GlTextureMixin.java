package net.typho.big_shot_lib.mixin.util;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack;
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL;
import net.typho.big_shot_lib.api.client.util.dynamic_buffers.DynamicBuffer;
import net.typho.big_shot_lib.api.util.buffers.BufferUploader;
import net.typho.big_shot_lib.impl.util.DynamicBufferRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;

@Mixin(GlTexture.class)
public abstract class GlTextureMixin extends GpuTexture {
    @Shadow
    public abstract int glId();

    public GlTextureMixin(int p_404771_, String p_405873_, TextureFormat p_405456_, int p_405638_, int p_404958_, int p_419943_, int p_423664_) {
        super(p_404771_, p_405873_, p_405456_, p_405638_, p_404958_, p_419943_, p_423664_);
    }

    @ModifyReturnValue(
            method = "createFbo",
            at = @At("RETURN")
    )
    private int getFbo(int glId) {
        GlTexture mainFboColor = (GlTexture) Minecraft.getInstance().getMainRenderTarget().getColorTexture();

        if (mainFboColor != null && mainFboColor.glId() == this.glId()) {
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

        return glId;
    }
}
