package net.typho.big_shot_lib.mixin;

import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.typho.big_shot_lib.api.peer.ShaderInstanceUnsafeExtension;
import net.typho.big_shot_lib.spirv.ShaderInstanceLocationsExtension;
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo;
import net.typho.big_shot_lib.spirv.ShaderMixinCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

@Mixin(ShaderInstance.class)
@Implements({
        @Interface(iface = ShaderInstanceLocationsExtension.class, prefix = "locations$"),
        @Interface(iface = ShaderInstanceUnsafeExtension.class, prefix = "unsafe$"),
})
public abstract class ShaderInstanceMixin {
    @Shadow
    @Final
    @Mutable
    private List<String> samplerNames;
    @Shadow
    @Final
    @Mutable
    private Map<String, IntSupplier> samplerMap;
    @Shadow
    @Final
    @Mutable
    private List<Integer> samplerLocations;
    @Shadow
    @Final
    @Mutable
    private List<Integer> uniformLocations;
    @Shadow
    @Final
    @Mutable
    private Map<String, Uniform> uniformMap;
    @Shadow
    @Final
    @Mutable
    private List<Uniform> uniforms;
    @Shadow
    @Final
    static Logger LOGGER;
    @Shadow
    @Final
    @Mutable
    private String name;
    @Shadow
    @Final
    @Mutable
    private int programId;
    @Shadow
    @Final
    @Mutable
    private Program vertexProgram;
    @Shadow
    @Final
    @Mutable
    private Program fragmentProgram;
    @Shadow
    @Final
    @Mutable
    private VertexFormat vertexFormat;
    @Shadow
    @Final
    @Mutable
    public Uniform MODEL_VIEW_MATRIX;
    @Shadow
    @Final
    @Mutable
    public Uniform PROJECTION_MATRIX;
    @Shadow
    @Final
    @Mutable
    public Uniform TEXTURE_MATRIX;
    @Shadow
    @Final
    @Mutable
    public Uniform SCREEN_SIZE;
    @Shadow
    @Final
    @Mutable
    public Uniform COLOR_MODULATOR;
    @Shadow
    @Final
    @Mutable
    public Uniform LIGHT0_DIRECTION;
    @Shadow
    @Final
    @Mutable
    public Uniform LIGHT1_DIRECTION;
    @Shadow
    @Final
    @Mutable
    public Uniform GLINT_ALPHA;
    @Shadow
    @Final
    @Mutable
    public Uniform FOG_START;
    @Shadow
    @Final
    @Mutable
    public Uniform FOG_END;
    @Shadow
    @Final
    @Mutable
    public Uniform FOG_COLOR;
    @Shadow
    @Final
    @Mutable
    public Uniform FOG_SHAPE;
    @Shadow
    @Final
    @Mutable
    public Uniform LINE_WIDTH;
    @Shadow
    @Final
    @Mutable
    public Uniform GAME_TIME;
    @Shadow
    @Final
    @Mutable
    public Uniform CHUNK_OFFSET;

    @Shadow
    @Nullable
    public abstract Uniform getUniform(String name);

    @Unique
    private ShaderLocationsInfo big_shot_lib$locations = ShaderMixinCallback.enabled ? new ShaderLocationsInfo(false) : null;

    @Inject(
            method = "updateLocations",
            at = @At("HEAD"),
            cancellable = true
    )
    private void updateLocations(CallbackInfo ci) {
        if (big_shot_lib$locations != null) {
            samplerMap.keySet().removeIf(name -> !big_shot_lib$locations.uniforms.map.containsKey(name));
            samplerLocations.clear();

            for (String sampler : samplerNames) {
                Integer location = big_shot_lib$locations.uniforms.get(sampler);

                if (location == null) {
                    LOGGER.warn("Shader {} could not find sampler named {} in the specified shader program.", name, sampler);
                    samplerMap.remove(sampler);
                } else {
                    samplerLocations.add(location);
                }
            }

            samplerNames.retainAll(samplerMap.keySet());

            uniformLocations.clear();
            uniformMap.clear();

            for (Uniform uniform : uniforms) {
                Integer location = big_shot_lib$locations.uniforms.get(uniform.getName());

                if (location == null) {
                    LOGGER.warn("Shader {} could not find uniform named {} in the specified shader program.", name, uniform.getName());
                } else {
                    uniformLocations.add(location);
                    uniform.setLocation(location);
                    uniformMap.put(uniform.getName(), uniform);
                }
            }

            ci.cancel();
        }
    }

    public @Nullable ShaderLocationsInfo locations$getLocations() {
        return big_shot_lib$locations;
    }

    public void locations$setLocations(ShaderLocationsInfo locations) {
        big_shot_lib$locations = locations;
    }

    public void unsafe$setProgramId(int id) {
        programId = id;
    }

    public void unsafe$setName(@NotNull String name) {
        this.name = name;
    }

    public void unsafe$setVertex(@NotNull Program program) {
        vertexProgram = program;
    }

    public void unsafe$setFragment(@NotNull Program program) {
        fragmentProgram = program;
    }

    public void unsafe$setFormat(@NotNull VertexFormat format) {
        vertexFormat = format;
    }

    public void unsafe$init() {
        big_shot_lib$locations = ShaderMixinCallback.enabled ? new ShaderLocationsInfo(false) : null;

        MODEL_VIEW_MATRIX = getUniform("ModelViewMat");
        PROJECTION_MATRIX = getUniform("ProjMat");
        TEXTURE_MATRIX = getUniform("TextureMat");
        SCREEN_SIZE = getUniform("ScreenSize");
        COLOR_MODULATOR = getUniform("ColorModulator");
        LIGHT0_DIRECTION = getUniform("Light0_Direction");
        LIGHT1_DIRECTION = getUniform("Light1_Direction");
        GLINT_ALPHA = getUniform("GlintAlpha");
        FOG_START = getUniform("FogStart");
        FOG_END = getUniform("FogEnd");
        FOG_COLOR = getUniform("FogColor");
        FOG_SHAPE = getUniform("FogShape");
        LINE_WIDTH = getUniform("LineWidth");
        GAME_TIME = getUniform("GameTime");
        CHUNK_OFFSET = getUniform("ChunkOffset");
    }
}
