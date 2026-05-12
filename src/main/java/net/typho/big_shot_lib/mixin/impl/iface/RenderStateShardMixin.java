package net.typho.big_shot_lib.mixin.impl.iface;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendingFactor;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlBlendShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction;
import net.typho.big_shot_lib.impl.util.MutableExtensionKt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if >=1.21.5 {
import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
//? }
@Mixin(RenderStateShard.class)
public class RenderStateShardMixin {
    @Shadow
    @Final
    public static RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY;

    @Shadow
    @Final
    public static RenderStateShard.TransparencyStateShard LIGHTNING_TRANSPARENCY;

    @Shadow
    @Final
    public static RenderStateShard.TransparencyStateShard GLINT_TRANSPARENCY;

    @Shadow
    @Final
    public static RenderStateShard.TransparencyStateShard CRUMBLING_TRANSPARENCY;

    @Shadow
    @Final
    public static RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY;

    static {
        MutableExtensionKt.setExtensionValue(ADDITIVE_TRANSPARENCY, new GlBlendShard.Enabled(
                new BlendFunction.Basic(
                        GlBlendingFactor.ONE,
                        GlBlendingFactor.ONE
                )
        ));
        MutableExtensionKt.setExtensionValue(LIGHTNING_TRANSPARENCY, new GlBlendShard.Enabled(
                new BlendFunction.Basic(
                        GlBlendingFactor.SRC_ALPHA,
                        GlBlendingFactor.ONE
                )
        ));
        MutableExtensionKt.setExtensionValue(GLINT_TRANSPARENCY, new GlBlendShard.Enabled(
                new BlendFunction.Separate(
                        GlBlendingFactor.SRC_ALPHA,
                        GlBlendingFactor.ONE,
                        GlBlendingFactor.ZERO,
                        GlBlendingFactor.ONE
                )
        ));
        MutableExtensionKt.setExtensionValue(CRUMBLING_TRANSPARENCY, new GlBlendShard.Enabled(
                new BlendFunction.Separate(
                        GlBlendingFactor.DST_COLOR,
                        GlBlendingFactor.SRC_COLOR,
                        GlBlendingFactor.ONE,
                        GlBlendingFactor.ZERO
                )
        ));
        MutableExtensionKt.setExtensionValue(TRANSLUCENT_TRANSPARENCY, new GlBlendShard.Enabled(
                new BlendFunction.Separate(
                        GlBlendingFactor.SRC_ALPHA,
                        GlBlendingFactor.ONE_MINUS_SRC_ALPHA,
                        GlBlendingFactor.ONE,
                        GlBlendingFactor.ONE_MINUS_SRC_ALPHA
                )
        ));
    }
}
