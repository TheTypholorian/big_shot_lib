package net.typho.big_shot_lib.mixin.impl.iface.texture;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlTexture2D;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

//? if >=1.21.5 {
/*import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.opengl.GlTexture;
*///? }

@Mixin(AbstractTexture.class)
public abstract class AbstractTextureMixin implements GlTexture2D {
    //? if <1.21.5 {
    @Shadow
    protected int id;
    //? } else {
    /*@Shadow
    @Nullable
    protected GpuTexture texture;
    *///? }

    @Shadow
    protected boolean blur;
    @Shadow
    protected boolean mipmap;

    @Shadow
    public abstract void setFilter(boolean blur, boolean mipmap);

    @Override
    public int getGlId() {
        //? if <1.21.5 {
        return id;
        //? } else {
        /*return texture == null ? -1 : ((GlTexture) texture).glId();
         *///? }
    }

    @Override
    public GlResourceType getType() {
        return GlResourceType.TEXTURE;
    }

    @Override
    public boolean getFreed() {
        return getGlId() == -1;
    }

    @Override
    public @NotNull GlTextureFormat getFormat() {
        return Objects.requireNonNull(getType().pushBoundValue(
                getGlId(),
                () -> GlTextureFormat.fromInternalId(glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT))
        ));
    }

    @Override
    public int getWidth() {
        return getType().pushBoundValue(
                getGlId(),
                () -> glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH)
        );
    }

    @Override
    public int getHeight() {
        return getType().pushBoundValue(
                getGlId(),
                () -> glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT)
        );
    }

    @Override
    public boolean getBlur() {
        return blur;
    }

    @Override
    public void setBlur(boolean b) {
        setFilter(b, mipmap);
    }

    @Override
    public boolean getMipmap() {
        return mipmap;
    }

    @Override
    public void setMipmap(boolean b) {
        setFilter(blur, b);
    }
}
