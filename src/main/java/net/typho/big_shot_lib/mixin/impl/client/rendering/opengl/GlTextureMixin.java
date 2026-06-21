package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl;

import com.mojang.blaze3d.opengl.GlTexture;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GlTexture.class)
public abstract class GlTextureMixin implements GlResource, GlNamed {
    @Shadow
    public abstract int glId();

    @Override
    public int getGlId() {
        return glId();
    }
}
