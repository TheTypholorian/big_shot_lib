package net.typho.big_shot_lib.mixin.impl.client.ext;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.typho.big_shot_lib.api.client.ext.VertexConsumerExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin extends VertexConsumerExtension {
}
