package net.typho.big_shot_lib.impl.mixin.iface;

import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture2D;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if <1.21.5 {
import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
//? }

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(GlTexture.class)
public abstract class GlTextureMixin extends GpuTexture implements ImmutableExtension<GlTexture2D> {
    //? if <1.21.6 {
    public GlTextureMixin(String string, TextureFormat textureFormat, int i, int j, int k) {
        super(string, textureFormat, i, j, k);
    }
    //? } else {
    /*public GlTextureMixin(int p_404771_, String p_405873_, TextureFormat p_405456_, int p_405638_, int p_404958_, int p_419943_, int p_423664_) {
        super(p_404771_, p_405873_, p_405456_, p_405638_, p_404958_, p_419943_, p_423664_);
    }
    *///? }

    @Shadow
    public abstract int glId();

    @Override
    public GlTexture2D getBig_shot_lib$extension_value() {
        return new GlTexture2D() {
            @Override
            public @Nullable GlTextureFormat getFormat() {
                return switch (GlTextureMixin.this.getFormat()) {
                    case RGBA8 -> GlTextureFormat.RGBA8;
                    case RED8 -> GlTextureFormat.R8;
                    case DEPTH32 -> GlTextureFormat.DEPTH_COMPONENT32;
                    //? if >=1.21.6 {
                    /*case RED8I -> GlTextureFormat.R8I;
                    *///? }
                    //? neoforge {
                    case DEPTH24_STENCIL8 -> GlTextureFormat.DEPTH24_STENCIL8;
                    case DEPTH32_STENCIL8 -> GlTextureFormat.DEPTH32F_STENCIL8;
                    //? }
                };
            }

            @Override
            public void free() {
                GlTextureMixin.this.close();
            }

            @Override
            public int getGlId() {
                return glId();
            }

            @Override
            public boolean getFreed() {
                return isClosed();
            }

            @Override
            public @NotNull GlResourceType getType() {
                return GlResourceType.TEXTURE;
            }

            @Override
            public int getWidth() {
                return GlTextureMixin.this.getWidth(0);
            }

            @Override
            public int getHeight() {
                return GlTextureMixin.this.getHeight(0);
            }

            @Override
            public @NotNull GlBoundTexture2D bind(@NotNull GlTextureTarget target) {
                return new BoundTexture(
                        this,
                        target,
                        NeoGlStateManager.Companion.getINSTANCE().getTextures().get(target).push(glId()),
                        getWidth(),
                        getHeight(),
                        getFormat()
                );
            }
        };
    }
}

class BoundTexture extends GlBoundTexture2D.Basic {
    private final int width, height;
    private final GlTextureFormat format;

    public BoundTexture(@NotNull GlTexture2D resource, @NotNull GlTextureTarget target, @NotNull GlStateStack.Handle<Integer> handle, int width, int height, GlTextureFormat format) {
        super(resource, target, handle);
        this.width = width;
        this.height = height;
        this.format = format;
    }

    @Override
    protected void resize(int width, int height, @NotNull GlTextureFormat format) {
        if (width != this.width || height != this.height) {
            throw new UnsupportedOperationException("Cannot resize a Minecraft GlTexture from " + this.width + "x" + this.height + " to " + width + "x" + height);
        }

        if (format != this.format) {
            throw new UnsupportedOperationException("Cannot change a Minecraft GlTexture's format from " + this.format + " to " + format);
        }
    }
}