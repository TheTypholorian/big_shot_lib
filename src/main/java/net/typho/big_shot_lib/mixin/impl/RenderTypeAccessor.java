package net.typho.big_shot_lib.mixin.impl;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//? if >=1.21 {
import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
//? }
@Mixin(RenderType.class)
public interface RenderTypeAccessor {
    @Accessor("sortOnUpload")
    boolean big_shot_lib$getSortOnUpload();
}
