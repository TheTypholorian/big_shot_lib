package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl;

import com.mojang.blaze3d.opengl.GlBuffer;
import com.mojang.blaze3d.opengl.GlTexture;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlResource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GlBuffer.class)
public abstract class GlBufferMixin implements GlResource, GlNamed {
    @Shadow
    public abstract int handle();

    @Override
    public int getGlId() {
        return handle();
    }
}
