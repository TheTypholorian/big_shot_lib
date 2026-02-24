package net.typho.big_shot_lib.mixin.util;

import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack;
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL;
import net.typho.big_shot_lib.api.client.util.dynamic_buffers.DynamicBuffer;
import net.typho.big_shot_lib.api.util.buffers.BufferUploader;
import net.typho.big_shot_lib.impl.util.DynamicBufferRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;

@Mixin(GlTexture.class)
public abstract class GlTextureMixin extends GpuTexture {
    @Shadow
    public abstract int glId();

    public GlTextureMixin(String string, TextureFormat textureFormat, int i, int j, int k) {
        super(string, textureFormat, i, j, k);
    }

    @ModifyArg(
            method = "getFbo",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/unimi/dsi/fastutil/ints/Int2IntMap;computeIfAbsent(ILit/unimi/dsi/fastutil/ints/Int2IntFunction;)I",
                    remap = false
            )
    )
    private Int2IntFunction getFbo(Int2IntFunction func) {
        return j -> {
            int glId = func.get(j);
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
        };
    }
}
