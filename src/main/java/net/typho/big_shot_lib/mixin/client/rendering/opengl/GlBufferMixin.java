package net.typho.big_shot_lib.mixin.client.rendering.opengl;

import com.mojang.blaze3d.opengl.GlBuffer;
import net.typho.big_shot_lib.client.api.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.client.api.rendering.opengl.GlResource;
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
