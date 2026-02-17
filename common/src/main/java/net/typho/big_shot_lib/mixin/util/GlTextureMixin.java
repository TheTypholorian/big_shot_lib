package net.typho.big_shot_lib.mixin.util;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL;
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebuffer;
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebufferAttachment;
import net.typho.big_shot_lib.api.client.rendering.textures.NeoTexture2D;
import net.typho.big_shot_lib.api.util.buffers.BufferUploader;
import net.typho.big_shot_lib.impl.util.WrapperUtilImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

@Mixin(GlTexture.class)
public abstract class GlTextureMixin extends GpuTexture {
    public GlTextureMixin(String string, TextureFormat textureFormat, int i, int j, int k) {
        super(string, textureFormat, i, j, k);
    }

    @ModifyArg(
            method = "getFbo",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/unimi/dsi/fastutil/ints/Int2IntMap;computeIfAbsent(ILit/unimi/dsi/fastutil/ints/Int2IntFunction;)I"
            )
    )
    private Int2IntFunction getFbo(
            Int2IntFunction func,
            @Local(argsOnly = true) @Nullable GpuTexture depth
    ) {
        return j -> {
            int glId = func.get(j);

            WrapperUtilImpl.fboCache.entrySet().stream()
                    .filter(entry -> entry.getKey().getColorTexture() == this)
                    .findFirst()
                    .ifPresent(entry -> {
                        GlFramebuffer neo = entry.getValue();

                        OpenGL.INSTANCE.bindFramebuffer(glId);

                        int k = 0;
                        List<Integer> buffers = new LinkedList<>();

                        for (GlFramebufferAttachment attachment : neo.getColorAttachments().subList(1, neo.getColorAttachments().size())) {
                            int point = GL_COLOR_ATTACHMENT0 + k++;
                            buffers.add(point);
                            attachment.resize(getWidth(0), getHeight(0), BufferUploader::uploadNull);
                            attachment.attachToFramebuffer(point);
                        }

                        OpenGL.INSTANCE.drawBuffers(buffers.isEmpty() ? new int[]{GL_NONE} : buffers.stream().mapToInt(l -> l).toArray());

                        if (depth != null) {
                            NeoTexture2D neoDepth = WrapperUtilImpl.mojangTextureToNeo(depth);
                            neoDepth.resize(getWidth(0), getHeight(0), BufferUploader::uploadNull);
                            neoDepth.attachToFramebuffer(
                                    Objects.requireNonNull(
                                            neoDepth.format().getDepthStencilAttachmentId(),
                                            neoDepth.format() + " is neither a depth nor stencil format"
                                    )
                            );
                        }

                        OpenGL.INSTANCE.bindFramebuffer(GlStateManager.getFrameBuffer(GL_FRAMEBUFFER));
                    });

            return glId;
        };
    }
}
