package net.typho.big_shot_lib.mixin.util;

import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBuffer;
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBufferRegistry;
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL;
import net.typho.big_shot_lib.api.util.buffers.BufferUploader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

@Mixin(GlTexture.class)
public abstract class GlTextureMixin extends GpuTexture {
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

            List<Integer> buffers = new LinkedList<>();
            buffers.add(GL_COLOR_ATTACHMENT0);

            for (Map.Entry<Integer, DynamicBuffer> entry : DynamicBufferRegistry.buffers.entrySet()) {
                int point = GL_COLOR_ATTACHMENT0 + entry.getKey();
                buffers.add(point);
                entry.getValue().resize(getWidth(0), getHeight(0), BufferUploader::uploadNull);
                entry.getValue().attachToFramebuffer(point);
            }

            OpenGL.INSTANCE.drawBuffers(buffers.stream().mapToInt(k -> k).toArray());

            return glId;
        };
    }
}
