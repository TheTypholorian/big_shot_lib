package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(VertexFormat.class)
public abstract class VertexFormatMixin implements NeoVertexFormat {
    @Shadow
    @Final
    private List<VertexFormatElement> elements;

    @NotNull
    @Override
    public Iterator<VertexFormatElement> iterator() {
        return elements.iterator();
    }
}
