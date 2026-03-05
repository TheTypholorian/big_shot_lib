package net.typho.big_shot_lib.mixin.util;

import com.mojang.blaze3d.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlStateManager.class)
public interface GlStateManagerAccessor {
    @Accessor("BLEND")
    static GlStateManager.BlendState getBlend() {
        return null;
    }

    @Accessor("DEPTH")
    static GlStateManager.DepthState getDepth() {
        return null;
    }

    @Accessor("CULL")
    static GlStateManager.CullState getCull() {
        return null;
    }

    @Accessor("POLY_OFFSET")
    static GlStateManager.PolygonOffsetState getPolygonOffset() {
        return null;
    }

    @Accessor("COLOR_LOGIC")
    static GlStateManager.ColorLogicState getColorLogic() {
        return null;
    }

    @Accessor("STENCIL")
    static GlStateManager.StencilState getStencil() {
        return null;
    }

    @Accessor("SCISSOR")
    static GlStateManager.ScissorState getScissor() {
        return null;
    }

    @Accessor("TEXTURES")
    static GlStateManager.TextureState[] getTextures() {
        return null;
    }

    @Accessor("COLOR_MASK")
    static GlStateManager.ColorMask getColorMask() {
        return null;
    }
}
