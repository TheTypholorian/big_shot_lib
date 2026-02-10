package net.typho.big_shot_lib.mixin.shaders.mixins;

import net.caffeinemc.mods.sodium.client.gl.attribute.GlVertexAttribute;
import net.caffeinemc.mods.sodium.client.gl.attribute.GlVertexFormat;
import net.caffeinemc.mods.sodium.client.render.vertex.VertexFormatAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Pseudo
@Mixin(GlVertexFormat.class)
public interface GlVertexFormatAccessor {
    @Accessor("attributesKeyed")
    Map<VertexFormatAttribute, GlVertexAttribute> getAttributes();
}
