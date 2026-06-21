package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendingFactor;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlLogicOp;
import net.typho.big_shot_lib.api.client.rendering.state.LayeringState;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction;
import net.typho.big_shot_lib.api.util.MutableExtensionKt;
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
    public static RenderStateShard.TransparencyStateShard NO_TRANSPARENCY;

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

    @Shadow
    @Final
    public static RenderStateShard.LayeringStateShard NO_LAYERING;

    @Shadow
    @Final
    public static RenderStateShard.LayeringStateShard POLYGON_OFFSET_LAYERING;

    @Shadow
    @Final
    public static RenderStateShard.LayeringStateShard VIEW_OFFSET_Z_LAYERING;

    @Shadow
    @Final
    public static RenderStateShard.ColorLogicStateShard NO_COLOR_LOGIC;

    @Shadow
    @Final
    public static RenderStateShard.ColorLogicStateShard OR_REVERSE_COLOR_LOGIC;

    static {
        MutableExtensionKt.setExtensionValue(NO_TRANSPARENCY, null);
        MutableExtensionKt.setExtensionValue(ADDITIVE_TRANSPARENCY, new BlendFunction.Basic(
                GlBlendingFactor.ONE,
                GlBlendingFactor.ONE
        ));
        MutableExtensionKt.setExtensionValue(LIGHTNING_TRANSPARENCY, new BlendFunction.Basic(
                GlBlendingFactor.SRC_ALPHA,
                GlBlendingFactor.ONE
        ));
        MutableExtensionKt.setExtensionValue(GLINT_TRANSPARENCY, new BlendFunction.Separate(
                GlBlendingFactor.SRC_ALPHA,
                GlBlendingFactor.ONE,
                GlBlendingFactor.ZERO,
                GlBlendingFactor.ONE
        ));
        MutableExtensionKt.setExtensionValue(CRUMBLING_TRANSPARENCY, new BlendFunction.Separate(
                GlBlendingFactor.DST_COLOR,
                GlBlendingFactor.SRC_COLOR,
                GlBlendingFactor.ONE,
                GlBlendingFactor.ZERO
        ));
        MutableExtensionKt.setExtensionValue(TRANSLUCENT_TRANSPARENCY, new BlendFunction.Separate(
                GlBlendingFactor.SRC_ALPHA,
                GlBlendingFactor.ONE_MINUS_SRC_ALPHA,
                GlBlendingFactor.ONE,
                GlBlendingFactor.ONE_MINUS_SRC_ALPHA
        ));

        MutableExtensionKt.setExtensionValue(NO_LAYERING, LayeringState.DISABLED);
        MutableExtensionKt.setExtensionValue(POLYGON_OFFSET_LAYERING, LayeringState.POLYGON_OFFSET);
        MutableExtensionKt.setExtensionValue(VIEW_OFFSET_Z_LAYERING, LayeringState.VIEW_OFFSET);

        MutableExtensionKt.setExtensionValue(NO_COLOR_LOGIC, null);
        MutableExtensionKt.setExtensionValue(OR_REVERSE_COLOR_LOGIC, GlLogicOp.OR_REVERSE);
    }
}
