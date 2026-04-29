package net.typho.big_shot_lib.mixin.impl;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

//? if <1.21 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(VertexFormatElement.class)
public interface VertexFormatElementAccessor {
    @Accessor("ELEMENTS")
    static List<VertexFormatElement> big_shot_lib$getElements() {
        throw new AssertionError();
    }
}
