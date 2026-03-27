package net.typho.big_shot_lib.impl.mixin.iface;

//? if >=1.21.5 {
/*import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTexture;
*///? }

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlTexture2D;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebufferAttachment;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager;
import net.typho.big_shot_lib.api.math.rect.AbstractRect2;
import net.typho.big_shot_lib.api.util.KeyedDelegate;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderTarget.class)
public abstract class RenderTargetMixin implements ImmutableExtension<GlFramebuffer> {
    //? if >=1.21.5 {
    /*@Shadow
    protected GpuTexture colorTexture;

    @Shadow
    protected GpuTexture depthTexture;
    *///? } else {
    @Shadow
    public int frameBufferId;

    @Shadow
    protected int colorTextureId;

    @Shadow
    protected int depthBufferId;

    @Shadow
    @Final
    public boolean useDepth;
    //? }

    @Shadow
    public abstract void destroyBuffers();

    @Override
    public GlFramebuffer getBig_shot_lib$extension_value() {
        return new GlFramebuffer() {
            @Override
            public int getGlId() {
                //? if >=1.21.5 {
                /*return ((GlTexture) colorTexture).getFbo(((GlDevice) RenderSystem.getDevice()).directStateAccess(), depthTexture);
                *///? } else {
                return frameBufferId;
                //? }
            }

            @Override
            public boolean getFreed() {
                //? if >=1.21.5 {
                /*return false;
                *///? } else {
                return frameBufferId == -1;
                //? }
            }

            @Override
            public GlResourceType getType() {
                return GlResourceType.FRAMEBUFFER;
            }

            @Override
            public void free() {
                destroyBuffers();
            }

            @Override
            public @NotNull KeyedDelegate.ReadOnly<Integer, GlFramebufferAttachment> getColorAttachments() {
                return KeyedDelegate.of(index -> {
                    if (index != 0) {
                        return null;
                    }

                    //? if >=1.21.5 {
                    /*return colorTexture == null ? null : ImmutableExtensionKt.getExtensionValue(colorTexture);
                    *///? } else {
                    return colorTextureId == -1 ? null : new NeoGlTexture2D(colorTextureId, false);
                    //? }
                });
            }

            @Override
            public @Nullable GlFramebufferAttachment getDepthAttachment() {
                //? if >=1.21.5 {
                /*return depthTexture == null ? null : ImmutableExtensionKt.getExtensionValue(depthTexture);
                 *///? } else {
                return useDepth && depthBufferId != -1 ? new NeoGlTexture2D(depthBufferId, false) : null;
                //? }
            }

            @Override
            public @NotNull GlBoundFramebuffer bind(@Nullable AbstractRect2<Integer> viewport) {
                return new BoundRenderTarget(
                        this,
                        viewport,
                        NeoGlStateManager.INSTANCE.getFramebuffer().push(getGlId())
                );
            }
        };
    }
}

class BoundRenderTarget extends GlBoundFramebuffer.Basic {
    public BoundRenderTarget(@NotNull GlFramebuffer resource, @Nullable AbstractRect2<Integer> viewport, @NotNull GlStateStack.Handle<Integer> handle) {
        super(resource, viewport, handle);
    }

    @Override
    @NotNull
    public KeyedDelegate<Integer, GlFramebufferAttachment> getColorAttachments() {
        return getResource().getColorAttachments().withSet((key, attachment) -> {
            throw new UnsupportedOperationException("Cannot modify a Minecraft RenderTarget's attachments");
        });
    }

    @Override
    @Nullable
    public GlFramebufferAttachment getDepthAttachment() {
        return getResource().getDepthAttachment();
    }

    @Override
    public void setDepthAttachment(@Nullable GlFramebufferAttachment glFramebufferAttachment) {
        throw new UnsupportedOperationException("Cannot modify a Minecraft RenderTarget's attachments");
    }
}