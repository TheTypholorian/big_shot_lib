package net.typho.big_shot_lib.mixin.impl;

//? if <1.21.10 {
/*import net.minecraft.client.renderer.RenderStateShard;
*///? } else if <1.21.11 {
/*import net.minecraft.client.renderer.RenderType;
*///? } else {
import net.minecraft.client.renderer.RenderType;
//? }

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//? if <1.21.10 {
/*@Mixin(RenderStateShard.class)
*///? } else {
@Mixin(RenderType.class)
//? }
public interface RenderTypeNameAccessor {
    @Accessor("name")
    String big_shot_lib$getName();
}
