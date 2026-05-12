package net.typho.big_shot_lib.mixin.impl.iface;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import kotlin.NotImplementedError;
import net.minecraft.client.renderer.RenderType;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.*;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction;
import net.typho.big_shot_lib.impl.IdentifierUtilKt;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.WrapperUtilImplKt;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if <1.21.5 {
/*import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
*///? } else {
import com.mojang.blaze3d.pipeline.RenderPipeline;
//? }

//? if >=1.21.11 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(RenderType.CompositeRenderType.class)
public class CompositeRenderTypeMixin implements ImmutableExtension<GlDrawState> {
    //? if >=1.21.5 {
    @Shadow
    @Final
    private RenderPipeline renderPipeline;
    //? }

    @Shadow
    @Final
    public RenderType.CompositeState state;

    @Override
    public GlDrawState getBig_shot_lib$extension_value() {
        return new GlDrawState() {
            @Override
            public @NotNull GlBlendShard getBlend() {
                //? if <1.21.5 {
                /*return ImmutableExtensionKt.getExtensionValue(state.transparencyState);
                *///? } else {
                return renderPipeline.getBlendFunction().<GlBlendShard>map(
                        function ->
                                new GlBlendShard.Enabled(
                                        new BlendFunction.Separate(
                                                WrapperUtilImplKt.getNeo(function.sourceColor()),
                                                WrapperUtilImplKt.getNeo(function.destColor()),
                                                WrapperUtilImplKt.getNeo(function.sourceAlpha()),
                                                WrapperUtilImplKt.getNeo(function.destAlpha())
                                        )
                                )
                ).orElse(GlBlendShard.Disabled.INSTANCE);
                //? }
            }

            @Override
            public @NotNull GlColorMaskShard getColorMask() {
                throw new NotImplementedError("Minecraft CompositeState get color mask");
            }

            @Override
            public @NotNull GlCullShard getCull() {
                throw new NotImplementedError("Minecraft CompositeState get cull");
            }

            @Override
            public @NotNull GlDepthShard getDepth() {
                throw new NotImplementedError("Minecraft CompositeState get depth");
            }

            @Override
            public @NotNull GlPolygonModeShard getPolygonMode() {
                throw new NotImplementedError("Minecraft CompositeState get polygon mode");
            }

            @Override
            public @NotNull GlPolygonOffsetShard getPolygonOffset() {
                throw new NotImplementedError("Minecraft CompositeState get polygon offset");
            }

            @Override
            public @NotNull GlScissorShard getScissor() {
                return GlScissorShard.Disabled.INSTANCE;
            }

            @Override
            public @NotNull GlShaderShard getShader() {
                return state.textureState.cutoutTexture().map(texture ->
                        new GlShaderShard.NoShader(new GlTextureBinding.FromLocation(
                                IdentifierUtilKt.getNeo(texture),
                                GlTextureTarget.TEXTURE_2D
                        ))
                ).orElseGet(GlShaderShard.NoShader::new);
            }

            @Override
            public @NotNull GlStencilShard getStencil() {
                return GlStencilShard.Disabled.INSTANCE;
            }
        };
    }
}
