package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin extends NeoVertexConsumer {
    @Shadow
    VertexConsumer addVertex(float v, float v1, float v2);

    @Shadow
    VertexConsumer setColor(int i, int i1, int i2, int i3);

    @Shadow
    VertexConsumer setColor(float p_350350_, float p_350356_, float p_350623_, float p_350312_);

    @Shadow
    VertexConsumer setColor(int p_350809_);

    @Shadow
    VertexConsumer setUv(float v, float v1);

    @Shadow
    VertexConsumer setNormal(float v, float v1, float v2);

    @Shadow
    VertexConsumer setUv2(int i, int i1);

    @Shadow
    VertexConsumer setUv1(int i, int i1);

    @Override
    @NotNull
    default NeoVertexConsumer vertex(float x, float y, float z) {
        return addVertex(x, y, z);
    }

    @Override
    @NotNull
    default NeoVertexConsumer color(int r, int g, int b, int a) {
        return setColor(r, g, b, a);
    }

    @Override
    @NotNull
    default NeoVertexConsumer color(float r, float g, float b, float a) {
        return setColor(r, g, b, a);
    }

    @Override
    @NotNull
    default NeoVertexConsumer color(int argb) {
        return setColor(argb);
    }

    @Override
    @NotNull
    default NeoVertexConsumer textureUV(float u, float v) {
        return setUv(u, v);
    }

    @Override
    @NotNull
    default NeoVertexConsumer overlayUV(int u, int v) {
        return setUv1(u, v);
    }

    @Override
    @NotNull
    default NeoVertexConsumer lightUV(int u, int v) {
        return setUv2(u, v);
    }

    @Override
    @NotNull
    default NeoVertexConsumer normal(float x, float y, float z) {
        return setNormal(x, y, z);
    }

    @Override
    default void end() {
        // TODO
    }
}
