package net.typho.big_shot_lib.mixin.impl;

//? if <1.21.10 {

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
//? if <1.21.10 {
@Mixin(RenderStateShard.class)
//? } else {
/*@Mixin(RenderType.class)
*///? }
public interface RenderTypeNameAccessor {
    @Accessor("name")
    String big_shot_lib$getName();
}
