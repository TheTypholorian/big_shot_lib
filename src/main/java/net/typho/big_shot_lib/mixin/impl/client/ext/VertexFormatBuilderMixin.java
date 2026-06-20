package net.typho.big_shot_lib.mixin.impl.client.ext;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.typho.big_shot_lib.api.client.ext.VertexFormatBuilderExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VertexFormat.Builder.class)
public class VertexFormatBuilderMixin implements VertexFormatBuilderExtension {
}
