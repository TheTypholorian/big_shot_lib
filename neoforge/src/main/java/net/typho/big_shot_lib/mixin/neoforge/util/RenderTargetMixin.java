package net.typho.big_shot_lib.mixin.neoforge.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL;
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebufferAttachment;
import net.typho.big_shot_lib.api.client.rendering.textures.GlTexture;
import net.typho.big_shot_lib.api.client.rendering.textures.NeoTexture2D;
import net.typho.big_shot_lib.api.client.rendering.textures.TextureFormat;
import net.typho.big_shot_lib.api.util.buffers.BufferUploader;
import net.typho.big_shot_lib.impl.util.RenderTargetExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;
import org.spongepowered.asm.mixin.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

@Mixin(value = RenderTarget.class, remap = false)
public abstract class RenderTargetMixin implements RenderTargetExtension {
    @Shadow
    @Final
    public boolean useDepth;
    @Unique
    private List<? extends @NotNull GlFramebufferAttachment> big_shot_lib$colorAttachments;
    @Unique
    private GlFramebufferAttachment big_shot_lib$depthAttachment;
    @Shadow
    public int viewWidth;
    @Shadow
    public int viewHeight;
    @Shadow
    protected int colorTextureId;
    @Shadow
    protected int depthBufferId;
    @Shadow
    public int frameBufferId;

    @Shadow
    public abstract void unbindRead();

    @Shadow
    public abstract void unbindWrite();

    @Shadow
    protected abstract void setFilterMode(int filterMode, boolean force);

    @Shadow
    public abstract void checkStatus();

    @Shadow
    public abstract void clear(boolean clearError);

    @Shadow
    public abstract void resize(int width, int height, boolean clearError);

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull List<@NotNull GlFramebufferAttachment> big_shot_lib$getColorAttachments() {
        if (big_shot_lib$colorAttachments == null) {
            big_shot_lib$colorAttachments = Collections.singletonList(new NeoTexture2D(TextureFormat.RGBA8));
        }

        return (List<GlFramebufferAttachment>) big_shot_lib$colorAttachments;
    }

    @Override
    public void big_shot_lib$setColorAttachments(@NotNull List<? extends @NotNull GlFramebufferAttachment> attachments) {
        big_shot_lib$colorAttachments = attachments;
        resize(viewWidth, viewHeight, Minecraft.ON_OSX);
    }

    @Override
    public @Nullable GlFramebufferAttachment big_shot_lib$getDepthAttachment() {
        if (big_shot_lib$depthAttachment == null && useDepth) {
            big_shot_lib$depthAttachment = new NeoTexture2D(TextureFormat.DEPTH_COMPONENT);
        }

        return big_shot_lib$depthAttachment;
    }

    @Override
    public void big_shot_lib$setDepthAttachment(@Nullable GlFramebufferAttachment attachment) {
        big_shot_lib$depthAttachment = attachment;
        resize(viewWidth, viewHeight, Minecraft.ON_OSX);
    }

    /**
     * @author The Typhothanian
     * @reason Dynamically modifying the attachments of a RenderTarget (ex. for dynamic buffers)
     */
    @Overwrite
    public void destroyBuffers() {
        RenderSystem.assertOnRenderThreadOrInit();
        unbindRead();
        unbindWrite();

        if (frameBufferId > -1) {
            OpenGL.INSTANCE.bindFramebuffer(0);
            OpenGL.INSTANCE.deleteFramebuffer(frameBufferId);
            frameBufferId = -1;
        }
    }

    /**
     * @author The Typhothanian
     * @reason Dynamically modifying the attachments of a RenderTarget (ex. for dynamic buffers)
     */
    @Overwrite
    public void createBuffers(int width, int height, boolean clearError) {
        frameBufferId = OpenGL.INSTANCE.createFramebuffer();
        OpenGL.INSTANCE.bindFramebuffer(frameBufferId);

        if (width <= 0) {
            width = viewWidth <= 0 ? 1 : viewWidth;
        }

        if (height <= 0) {
            height = viewHeight <= 0 ? 1 : viewHeight;
        }

        int i = 0;
        List<Integer> buffers = new LinkedList<>();

        for (GlFramebufferAttachment attachment : big_shot_lib$getColorAttachments()) {
            int point = GL_COLOR_ATTACHMENT0 + i++;
            buffers.add(point);
            attachment.resize(width, height, BufferUploader::uploadNull);
            attachment.attachToFramebuffer(point);
        }

        OpenGL.INSTANCE.drawBuffers(buffers.isEmpty() ? new int[]{GL_NONE} : buffers.stream().mapToInt(j -> j).toArray());

        GlFramebufferAttachment depth = big_shot_lib$getDepthAttachment();

        if (depth != null) {
            depth.resize(width, height, BufferUploader::uploadNull);
            depth.attachToFramebuffer(
                    Objects.requireNonNull(
                            depth.format().getDepthStencilAttachmentId(),
                            depth.format() + " is neither a depth nor stencil format"
                    )
            );
        }

        colorTextureId = ((GlTexture) big_shot_lib$colorAttachments.getFirst()).glId();
        depthBufferId = depth == null ? -1 : ((GlTexture) depth).glId();

        setFilterMode(GL_NEAREST, true);

        checkStatus();
        clear(clearError);
        unbindRead();
    }

    /**
     * @author The Typhothanian
     * @reason Dynamically modifying the attachments of a RenderTarget (ex. for dynamic buffers)
     */
    @Overwrite
    public void enableStencil() {
        if (!isStencilEnabled()) {
            if (big_shot_lib$depthAttachment instanceof NativeResource depth) {
                depth.free();
            }

            big_shot_lib$depthAttachment = useDepth ? new NeoTexture2D(TextureFormat.DEPTH32F_STENCIL8) : null;
            resize(viewWidth, viewHeight, Minecraft.ON_OSX);
        }
    }

    /**
     * @author The Typhothanian
     * @reason Dynamically modifying the attachments of a RenderTarget (ex. for dynamic buffers)
     */
    @Overwrite
    public boolean isStencilEnabled() {
        if (big_shot_lib$depthAttachment == null) {
            return false;
        }

        return big_shot_lib$depthAttachment.format().hasStencil;
    }
}
