package net.typho.big_shot_lib.mixin.impl.client.ext;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.typho.big_shot_lib.api.client.ext.VertexConsumerExtension;
import net.typho.big_shot_lib.api.util.buffer.NativeDataOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(VertexMultiConsumer.Double.class)
public abstract class VertexDoubleConsumerMixin implements VertexConsumer, VertexConsumerExtension {
    @Shadow
    @Final
    private VertexConsumer first;
    @Shadow
    @Final
    private VertexConsumer second;

    @Override
    public boolean customElementsSupported() {
        return first.customElementsSupported() || second.customElementsSupported();
    }

    @Override
    @NotNull
    public VertexConsumer custom(@NotNull VertexFormatElement element, @NotNull Consumer<NativeDataOutput> out) {
        first.custom(element, out);
        second.custom(element, out);

        return this;
    }
}
