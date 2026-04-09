package net.typho.big_shot_lib.mixin.impl.iface;

//? if >=1.21.5 {
/*import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTexture;
*///? }

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlTexture2D;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebufferAttachment;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager;
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext;
import net.typho.big_shot_lib.api.math.rect.AbstractRect2;
import net.typho.big_shot_lib.api.util.KeyedDelegate;
import net.typho.big_shot_lib.impl.client.rendering.internal.BoundMinecraftRenderTarget;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
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

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Unique
    private final Thread big_shot_lib$thread = Thread.currentThread();

    @Override
    public GlFramebuffer getBig_shot_lib$extension_value() {
        return new GlFramebuffer() {
            @Override
            public RenderingContext getContext() {
                return RenderingContext.MAIN;
            }

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
                /*return colorTexture == null;
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
                    return colorTextureId == -1 ? null : new NeoGlTexture2D(colorTextureId, false, GlTextureFormat.RGBA8, width, height, RenderingContext.MAIN);
                    //? }
                });
            }

            @Override
            public @Nullable GlFramebufferAttachment getDepthAttachment() {
                //? if >=1.21.5 {
                /*return depthTexture == null ? null : ImmutableExtensionKt.getExtensionValue(depthTexture);
                 *///? } else {
                return useDepth && depthBufferId != -1 ? new NeoGlTexture2D(depthBufferId, false, GlTextureFormat.DEPTH_COMPONENT, width, height, RenderingContext.MAIN) : null;
                //? }
            }

            @Override
            public @NotNull GlBoundFramebuffer bind(@Nullable AbstractRect2<Integer> viewport) {
                return new BoundMinecraftRenderTarget(
                        this,
                        viewport,
                        NeoGlStateManager.Companion.getCURRENT().getFramebuffer().push(getGlId())
                );
            }
        };
    }
}