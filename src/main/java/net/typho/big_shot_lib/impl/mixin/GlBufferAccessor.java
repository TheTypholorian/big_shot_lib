package net.typho.big_shot_lib.impl.mixin;

import com.mojang.blaze3d.opengl.GlBuffer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.kikugie.fletching_table.annotation.MixinIgnore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//? if <1.21.5 {
@MixinIgnore
//? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(GlBuffer.class)
public interface GlBufferAccessor {
    @Accessor("handle")
    int big_shot_lib$getHandle();
}
