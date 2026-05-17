package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.vertex.VertexFormat;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import kotlin.Pair;
import kotlin.Unit;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlPolygonMode;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.*;
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat;
import net.typho.big_shot_lib.api.util.resource.Identifier;
import net.typho.big_shot_lib.impl.IdentifierUtilKt;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

//? if >=1.21.11 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(RenderType.CompositeRenderType.class)
public abstract class CompositeRenderTypeMixin extends RenderType implements ImmutableExtension<NeoRenderTypeExtensionValue> {
    //? if >=1.21.5 {
    /*@Shadow
    @Final
    private RenderPipeline renderPipeline;
    *///? }

    @Shadow
    @Final
    private CompositeState state;

    @Shadow
    @Final
    private boolean isOutline;

    @Shadow
    @Final
    private Optional<RenderType> outline;

    public CompositeRenderTypeMixin(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    @Unique
    private final GlDrawState big_shot_lib$drawState = new GlDrawState() {
        @Override
        public @NotNull GlBlendShard getBlend() {
            //? if <1.21.5 {
            return ImmutableExtensionKt.getExtensionValue(state.transparencyState, GlBlendShard.class);
            //? } else {
                /*return renderPipeline.getBlendFunction().<GlBlendShard>map(
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
                *///? }
        }

        @Override
        public @NotNull GlColorMaskShard getColorMask() {
            return new GlColorMaskShard((boolean) ImmutableExtensionKt.getExtensionValue(state.writeMaskState, Pair.class).getFirst()); // TODO
        }

        @Override
        public @NotNull GlCullShard getCull() {
            return ImmutableExtensionKt.getExtensionValue(state.cullState, GlCullShard.class);
        }

        @Override
        public @NotNull GlDepthShard getDepth() {
            GlAlphaFunction function = ImmutableExtensionKt.getExtensionValue(state.depthTestState, GlAlphaFunction.class);

            if (function == null) {
                return GlDepthShard.Disabled.INSTANCE;
            } else {
                return new GlDepthShard.Enabled(
                        function,
                        (boolean) ImmutableExtensionKt.getExtensionValue(state.writeMaskState, Pair.class).getSecond()
                );
            }
        }

        @Override
        public @NotNull GlLayeringShard getLayering() {
            return ImmutableExtensionKt.getExtensionValue(state.layeringState, GlLayeringShard.class);
        }

        @Override
        public @NotNull GlLightmapShard getLightmap() {
            return ImmutableExtensionKt.getExtensionValue(state.lightmapState, GlLightmapShard.class);
        }

        @Override
        public @NotNull GlOverlayShard getOverlay() {
            return ImmutableExtensionKt.getExtensionValue(state.overlayState, GlOverlayShard.class);
        }

        @Override
        public @NotNull GlShaderShard getShader() {
            GlTextureBinding[] textures = new GlTextureBinding[12];
            GlTextureBinding[] from = ImmutableExtensionKt.getExtensionValueNullable(state.textureState, GlTextureBinding[].class);

            if (from == null) {
                state.textureState.cutoutTexture().ifPresent(texture -> textures[0] = new GlTextureBinding.FromLocation(
                        IdentifierUtilKt.getNeo(texture),
                        GlTextureTarget.TEXTURE_2D
                ));
            } else {
                System.arraycopy(from, 0, textures, 0, from.length);
            }

            return state.shaderState.shader.<GlShaderShard>map(shader ->
                    new GlShaderShard.FromInstance(
                            () -> ImmutableExtensionKt.getExtensionValue(shader.get(), GlProgram.class),
                            program -> { },
                            textures
                    )
            ).orElseGet(() -> new GlShaderShard.NoShader(textures));
        }
    };
    @Unique
    private final NeoRenderTypeExtensionValue big_shot_lib$extension_value = new NeoRenderTypeExtensionValue() {
        @Override
        @NotNull
        public NeoVertexFormat getFormat() {
            return ImmutableExtensionKt.getExtensionValue(format, NeoVertexFormat.class);
        }

        @Override
        @NotNull
        public GlBeginMode getMode() {
            return switch (mode) {
                case LINES, DEBUG_LINES -> GlBeginMode.LINES;
                case LINE_STRIP, DEBUG_LINE_STRIP -> GlBeginMode.LINE_STRIP;
                case TRIANGLES -> GlBeginMode.TRIANGLES;
                case TRIANGLE_STRIP -> GlBeginMode.TRIANGLE_STRIP;
                case TRIANGLE_FAN -> GlBeginMode.TRIANGLE_FAN;
                case QUADS -> GlBeginMode.QUADS;
            };
        }

        @Override
        public int getDefaultBufferSize() {
            return bufferSize;
        }

        @Override
        public boolean getAffectsCrumbling() {
            return affectsCrumbling;
        }

        @Override
        public boolean getSortOnUpload() {
            return sortOnUpload;
        }

        @Override
        @Nullable
        public NeoRenderType getOutlineSettings() {
            return outline.map(type -> ImmutableExtensionKt.getExtensionValue(type, NeoRenderType.class)).orElse(null);
        }

        @Override
        public boolean isOutline() {
            return isOutline;
        }

        @Override
        @NotNull
        public GlDrawState getDrawState() {
            return big_shot_lib$drawState;
        }

        @Override
        @Nullable
        public Identifier getLocation() {
            return Identifier.tryParse(name);
        }

        @Override
        @NotNull
        public RenderType getBig_shot_lib$extension_value() {
            return CompositeRenderTypeMixin.this;
        }
    };

    @Override
    public NeoRenderTypeExtensionValue getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
