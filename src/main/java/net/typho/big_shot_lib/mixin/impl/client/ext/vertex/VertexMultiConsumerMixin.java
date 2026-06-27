package net.typho.big_shot_lib.mixin.impl.client.ext.vertex;

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

@Mixin(VertexMultiConsumer.Multiple.class)
public abstract class VertexMultiConsumerMixin implements VertexConsumer, VertexConsumerExtension {
    @Shadow
    @Final
    private VertexConsumer[] delegates;

    @Override
    public boolean customElementsSupported() {
        for (VertexConsumer delegate : delegates) {
            if (delegate.customElementsSupported()) {
                return true;
            }
        }

        return false;
    }

    @Override
    @NotNull
    public VertexConsumer custom(@NotNull VertexFormatElement element, @NotNull Consumer<NativeDataOutput> out) {
        for (VertexConsumer delegate : delegates) {
            delegate.custom(element, out);
        }

        return this;
    }
}
