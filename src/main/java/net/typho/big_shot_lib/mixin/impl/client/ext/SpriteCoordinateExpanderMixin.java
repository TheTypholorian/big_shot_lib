package net.typho.big_shot_lib.mixin.impl.client.ext;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.SpriteCoordinateExpander;
import net.typho.big_shot_lib.api.client.ext.VertexConsumerExtension;
import net.typho.big_shot_lib.api.util.buffer.NativeDataOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(SpriteCoordinateExpander.class)
public abstract class SpriteCoordinateExpanderMixin implements VertexConsumer, VertexConsumerExtension {
    @Shadow
    @Final
    private VertexConsumer delegate;

    @Override
    public boolean customElementsSupported() {
        return delegate.customElementsSupported();
    }

    @Override
    @NotNull
    public VertexConsumer custom(@NotNull VertexFormatElement element, @NotNull Consumer<NativeDataOutput> out) {
        delegate.custom(element, out);
        return this;
    }
}
