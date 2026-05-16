package net.typho.big_shot_lib.mixin.impl.iface;

import kotlin.Pair;
import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendingFactor;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlBlendShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlColorMaskShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlLayeringShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset;
import net.typho.big_shot_lib.api.math.vec.NeoVec3f;
import net.typho.big_shot_lib.impl.util.MutableExtensionKt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
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
    public static RenderStateShard.WriteMaskStateShard COLOR_DEPTH_WRITE;

    @Shadow
    @Final
    public static RenderStateShard.WriteMaskStateShard COLOR_WRITE;

    @Shadow
    @Final
    public static RenderStateShard.WriteMaskStateShard DEPTH_WRITE;

    @Shadow
    @Final
    public static RenderStateShard.LayeringStateShard NO_LAYERING;

    @Shadow
    @Final
    public static RenderStateShard.LayeringStateShard POLYGON_OFFSET_LAYERING;

    @Shadow
    @Final
    public static RenderStateShard.LayeringStateShard VIEW_OFFSET_Z_LAYERING;

    static {
        MutableExtensionKt.setExtensionValue(NO_TRANSPARENCY, GlBlendShard.Disabled.INSTANCE);
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

        MutableExtensionKt.setExtensionValue(COLOR_DEPTH_WRITE, new Pair<>(new GlColorMaskShard(ColorMask.ENABLED), true));
        MutableExtensionKt.setExtensionValue(COLOR_WRITE, new Pair<>(new GlColorMaskShard(ColorMask.ENABLED), false));
        MutableExtensionKt.setExtensionValue(DEPTH_WRITE, new Pair<>(new GlColorMaskShard(ColorMask.DISABLED), true));

        MutableExtensionKt.setExtensionValue(NO_LAYERING, GlLayeringShard.Disabled.INSTANCE);
        MutableExtensionKt.setExtensionValue(POLYGON_OFFSET_LAYERING, new GlLayeringShard.EnabledPolygonOffset(new PolygonOffset(-1, -10)));
        MutableExtensionKt.setExtensionValue(VIEW_OFFSET_Z_LAYERING, new GlLayeringShard.EnabledViewOffset(new NeoVec3f(0.99975586f, 0.99975586f, 0.99975586f)));
    }
}
