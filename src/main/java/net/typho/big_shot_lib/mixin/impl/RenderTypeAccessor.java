package net.typho.big_shot_lib.mixin.impl;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.kikugie.fletching_table.annotation.MixinIgnore;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinIgnore
//? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(RenderType.class)
public interface RenderTypeAccessor {
    @Accessor("sortOnUpload")
    boolean big_shot_lib$getSortOnUpload();
}
