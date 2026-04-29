package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.textures.GpuSampler;
import kotlin.NotImplementedError;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.*;
import net.typho.big_shot_lib.impl.IdentifierUtilKt;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

//? if <1.21.11 {
import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
//? }
@Mixin(RenderSetup.class)
public class RenderSetupMixin implements ImmutableExtension<GlDrawState> {
    @Shadow
    @Final
    public Map<String, RenderSetup.TextureBinding> textures;

    @Override
    public GlDrawState getBig_shot_lib$extension_value() {
        return new GlDrawState() {
            @Override
            public @NotNull GlBlendShard getBlend() {
                throw new NotImplementedError("Minecraft RenderType get blend");
            }

            @Override
            public @NotNull GlColorMaskShard getColorMask() {
                throw new NotImplementedError("Minecraft RenderType get color mask");
            }

            @Override
            public @NotNull GlCullShard getCull() {
                throw new NotImplementedError("Minecraft RenderType get cull");
            }

            @Override
            public @NotNull GlDepthShard getDepth() {
                throw new NotImplementedError("Minecraft RenderType get depth");
            }

            @Override
            public @NotNull GlPolygonModeShard getPolygonMode() {
                throw new NotImplementedError("Minecraft RenderType get polygon mode");
            }

            @Override
            public @NotNull GlPolygonOffsetShard getPolygonOffset() {
                throw new NotImplementedError("Minecraft RenderType get polygon offset");
            }

            @Override
            public @NotNull GlScissorShard getScissor() {
                return GlScissorShard.Disabled.INSTANCE;
            }

            @Override
            public @NotNull GlShaderShard getShader() {
                GlTextureBinding[] neoTextures = new GlTextureBinding[textures.size()];

                textures.entrySet().stream()
                        .filter(entry -> {
                            if (entry.getKey().startsWith("Sampler")) {
                                try {
                                    GpuSampler gpuSampler = entry.getValue().sampler().get();
                                    neoTextures[Integer.parseInt(entry.getKey().substring("Sampler".length()))] = gpuSampler == null ? new GlTextureBinding.FromLocation(
                                            IdentifierUtilKt.getNeo(entry.getValue().location()),
                                            GlTextureTarget.TEXTURE_2D
                                    ) : new GlTextureBinding.FromLocation(
                                            IdentifierUtilKt.getNeo(entry.getValue().location()),
                                            GlTextureTarget.TEXTURE_2D,
                                            ImmutableExtensionKt.getExtensionValue(gpuSampler)
                                    );
                                    return false;
                                } catch (NumberFormatException ignored) {
                                }
                            }
                            return true;
                        })
                        .forEachOrdered(entry -> {
                        });

                return new GlShaderShard.NoShader(neoTextures);
            }

            @Override
            public @NotNull GlStencilShard getStencil() {
                return GlStencilShard.Disabled.INSTANCE;
            }
        };
    }
}
