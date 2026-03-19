package net.typho.big_shot_lib.impl.mixin;

//? if <1.21 {
/*import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.gen.Accessor;
*///? }

import com.mojang.blaze3d.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(VertexFormat.class)
public interface VertexFormatAccessor {
    //? if <1.21 {
    /*@Accessor("offsets")
    IntList big_shot_lib$getOffsets();

    @Accessor("elementMapping")
    ImmutableMap<String, VertexFormatElement> big_shot_lib$getElementMapping();
    *///? }
}
