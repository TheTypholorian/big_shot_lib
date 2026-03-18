package net.typho.big_shot_lib.impl.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(VertexFormat.class)
public interface VertexFormatAccessor {
    @Accessor("offsets")
    IntList big_shot_lib$getOffsets();

    @Accessor("elementMapping")
    ImmutableMap<String, VertexFormatElement> big_shot_lib$getElementMapping();
}
