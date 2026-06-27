package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.pipeline.GpuFramebufferImpl;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.common.GpuTexture;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.NeoGpuFramebuffer;
import net.typho.big_shot_lib.impl.client.rendering.opengl.state.NeoGlStateManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GpuFramebufferImpl.class)
public abstract class GpuFramebufferImplMixin implements NeoGpuFramebuffer {
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    @Final
    public boolean useDepth;
    @Shadow
    protected int colorTextureId;
    @Shadow
    protected int depthBufferId;

    @Shadow
    public abstract void resize(int width, int height, boolean clearError);

    @Shadow
    public abstract void createBuffers(int width, int height, boolean clearError);

    @Override
    public @NotNull GpuTexture getColorTexture() {
        return new GpuTexture() {
            @Override
            public GlResourceType getType() {
                return GlResourceType.TEXTURE;
            }

            @Override
            public boolean getFreed() {
                return getGlId() == -1;
            }

            @Override
            public int getGlId() {
                return colorTextureId;
            }

            @Override
            public void close() {
                if (colorTextureId > -1) {
                    TextureUtil.releaseTextureId(colorTextureId);
                    colorTextureId = -1;
                }
            }

            @NotNull
            @Override
            public GlTextureFormat getFormat() {
                return GlTextureFormat.RGBA;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public boolean getBlur() {
                return false;
            }

            @Override
            public void setBlur(boolean b) {
            }

            @Override
            public boolean getMipmap() {
                return false;
            }

            @Override
            public void setMipmap(boolean b) {
            }
        };
    }

    @Override
    public void setColorTexture(@NotNull GpuTexture glTexture2D) {
        throw new UnsupportedOperationException("NeoGpuFramebuffer.setColorTexture");
    }

    @Override
    @Nullable
    public GpuTexture getDepthTexture() {
        return useDepth ? new GpuTexture() {
            @Override
            public GlResourceType getType() {
                return GlResourceType.TEXTURE;
            }

            @Override
            public boolean getFreed() {
                return getGlId() == -1;
            }

            @Override
            public int getGlId() {
                return depthBufferId;
            }

            @Override
            public void close() {
                if (depthBufferId > -1) {
                    TextureUtil.releaseTextureId(depthBufferId);
                    depthBufferId = -1;
                }
            }

            @Override
            @NotNull
            public GlTextureFormat getFormat() {
                return GlTextureFormat.DEPTH_COMPONENT;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public boolean getBlur() {
                return false;
            }

            @Override
            public void setBlur(boolean b) {
            }

            @Override
            public boolean getMipmap() {
                return false;
            }

            @Override
            public void setMipmap(boolean b) {
            }
        } : null;
    }

    @Override
    public void setDepthTexture(@Nullable GpuTexture glTexture2D) {
        throw new UnsupportedOperationException("NeoGpuFramebuffer.setDepthTexture");
    }

    @Override
    public void resize(int width, int height) {
        resize(width, height, Minecraft.ON_OSX);
    }

    @Override
    public void createBuffers(int width, int height) {
        createBuffers(width, height, Minecraft.ON_OSX);
    }

    @Override
    public void clear(@NotNull GlClearBit... bits) {
        GlStateManager._clear(GlClearBit.initAndGetMask(bits), Minecraft.ON_OSX);
    }

    @Inject(
            method = "_bindWrite",
            at = @At("TAIL")
    )
    private void bindWrite(boolean setViewport, CallbackInfo ci) {
        NeoGlStateManagerImpl.boundFramebuffer = (GpuFramebufferImpl) (Object) this;
    }

    @Inject(
            method = "unbindWrite",
            at = @At("TAIL")
    )
    private void unbindWrite(CallbackInfo ci) {
        NeoGlStateManagerImpl.boundFramebuffer = null;
    }
}
