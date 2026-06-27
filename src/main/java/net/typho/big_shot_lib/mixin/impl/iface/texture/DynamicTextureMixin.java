package net.typho.big_shot_lib.mixin.impl.iface.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat;
import net.typho.big_shot_lib.api.client.rendering.common.GpuTexture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static org.lwjgl.opengl.GL11.*;

@Mixin(DynamicTexture.class)
public abstract class DynamicTextureMixin implements GpuTexture {
    @Shadow
    @Nullable
    private NativeImage pixels;

    @Override
    public @NotNull GlTextureFormat getFormat() {
        if (pixels != null) {
            return GlNamed.getEnum(GlTextureFormat.class, pixels.format().glFormat());
        }

        return getType().pushBoundValue(
                getGlId(),
                () -> GlTextureFormat.fromInternalId(glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT))
        );
    }

    @Override
    public int getWidth() {
        if (pixels != null) {
            return pixels.getWidth();
        }

        return getType().pushBoundValue(
                getGlId(),
                () -> glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH)
        );
    }

    @Override
    public int getHeight() {
        if (pixels != null) {
            return pixels.getHeight();
        }

        return getType().pushBoundValue(
                getGlId(),
                () -> glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT)
        );
    }
}
