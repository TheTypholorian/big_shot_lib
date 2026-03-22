package net.typho.big_shot_lib.impl.mixin;

import com.mojang.blaze3d.opengl.GlBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlBuffer.class)
public interface GlBufferAccessor {
    @Accessor("handle")
    int big_shot_lib$getHandle();
}
