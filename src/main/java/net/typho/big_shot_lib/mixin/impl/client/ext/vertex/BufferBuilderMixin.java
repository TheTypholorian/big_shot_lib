package net.typho.big_shot_lib.mixin.impl.client.ext.vertex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.typho.big_shot_lib.api.client.ext.VertexConsumerExtension;
import net.typho.big_shot_lib.api.util.buffer.MemoryPointer;
import net.typho.big_shot_lib.api.util.buffer.MemoryWriter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(BufferBuilder.class)
public abstract class BufferBuilderMixin implements VertexConsumer, VertexConsumerExtension {
    @Shadow
    protected abstract long beginElement(VertexFormatElement element);

    @Override
    public boolean customElementsSupported() {
        return true;
    }

    @Override
    @NotNull
    public VertexConsumer custom(@NotNull VertexFormatElement element, @NotNull Consumer<MemoryWriter> out) {
        long ptr = beginElement(element);

        if (ptr != -1) {
            out.accept(MemoryPointer.wrap(ptr, element.format().blockSize()).write());
        }

        return this;
    }
}
