package net.typho.big_shot_lib.mixin.impl;

//? if <1.21.11 {
import net.minecraft.client.renderer.RenderStateShard;
//? } else {
/*import net.minecraft.client.renderer.rendertype.RenderType;
*///? }

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
//? if <1.21.11 {
@Mixin(RenderStateShard.class)
//? } else {
/*@Mixin(RenderType.class)
*///? }
public interface RenderTypeNameAccessor {
    @Accessor("name")
    String big_shot_lib$getName();
}
