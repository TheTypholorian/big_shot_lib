package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.NeoRenderTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderTarget.class)
public abstract class RenderTargetMixin implements NeoRenderTarget {
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
    public abstract void resize(int par1, int par2, boolean par3);

    @Shadow
    public abstract void createBuffers(int par1, int par2, boolean par3);

    @Shadow
    public abstract void setClearColor(float red, float green, float blue, float alpha);

    @Override
    public @NotNull GlTexture2D getColorTexture() {
        return new GlTexture2D() {
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
    public void setColorTexture(@NotNull GlTexture2D glTexture2D) {
        throw new UnsupportedOperationException("NeoRenderTarget.setColorTexture");
    }

    @Override
    @Nullable
    public GlTexture2D getDepthTexture() {
        return useDepth ? new GlTexture2D() {
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
    public void setDepthTexture(@Nullable GlTexture2D glTexture2D) {
        throw new UnsupportedOperationException("NeoRenderTarget.setDepthTexture");
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
}
