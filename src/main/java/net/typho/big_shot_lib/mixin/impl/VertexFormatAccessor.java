package net.typho.big_shot_lib.mixin.impl;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.kikugie.fletching_table.annotation.MixinIgnore;
import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinIgnore
//? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(VertexFormat.class)
public interface VertexFormatAccessor {
    @Accessor("offsets")
    IntList big_shot_lib$getOffsets();

    @Accessor("elementMapping")
    ImmutableMap<String, VertexFormatElement> big_shot_lib$getElementMapping();
}
