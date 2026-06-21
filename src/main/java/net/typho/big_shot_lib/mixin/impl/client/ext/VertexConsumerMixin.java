package net.typho.big_shot_lib.mixin.impl.client.ext;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.typho.big_shot_lib.api.client.ext.VertexConsumerExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin extends VertexConsumerExtension {
    //? if <1.21.11 {
    /*@Override
    default @NotNull VertexConsumer setLineWidth(float width) {
        throw new UnsupportedOperationException("setLineWidth is only available in 1.21.11 and above. When creating lines, you must have two separate setups.");
    }
    *///? }
}
