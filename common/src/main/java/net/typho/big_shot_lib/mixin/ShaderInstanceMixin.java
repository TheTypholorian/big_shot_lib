package net.typho.big_shot_lib.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.typho.big_shot_lib.api.peer.ShaderInstanceUnsafeExtension;
import net.typho.big_shot_lib.spirv.ShaderInstanceLocationsExtension;
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo;
import net.typho.big_shot_lib.spirv.ShaderMixinCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@Mixin(ShaderInstance.class)
@Implements({
        @Interface(iface = ShaderInstanceLocationsExtension.class, prefix = "locations$"),
        @Interface(iface = ShaderInstanceUnsafeExtension.class, prefix = "unsafe$")
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

    @Shadow
    public abstract void markDirty();

    @Shadow
    protected abstract void updateLocations();

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

    public @NotNull List<Uniform> unsafe$getUniforms() {
        return uniforms;
    }

    public void unsafe$initialize() {
        samplerMap = Maps.newHashMap();
        samplerNames = Lists.newArrayList();
        samplerLocations = Lists.newArrayList();
        uniforms = Lists.newArrayList();
        uniformLocations = Lists.newArrayList();
        uniformMap = Maps.newHashMap();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer sizeBuf = stack.mallocInt(1);
            IntBuffer typeBuf = stack.mallocInt(1);
            int numUniforms = glGetProgrami(programId, GL_ACTIVE_UNIFORMS);

            for (int i = 0; i < numUniforms; i++) {
                String name = glGetActiveUniform(programId, i, sizeBuf, typeBuf);
                int type = typeBuf.get(0);

                switch (type) {
                    case GL_SAMPLER_2D,
                         GL_SAMPLER_3D,
                         GL_SAMPLER_CUBE,
                         GL_SAMPLER_2D_SHADOW,
                         GL_INT_SAMPLER_2D,
                         GL_UNSIGNED_INT_SAMPLER_2D,
                         GL_SAMPLER_2D_ARRAY,
                         GL_SAMPLER_CUBE_SHADOW,
                         GL_SAMPLER_2D_ARRAY_SHADOW: {
                        samplerMap.put(name, null);
                        samplerNames.add(name);
                        break;
                    }
                    default: {
                        int index = switch (type) {
                            case GL_INT_VEC2, GL_INT_VEC3, GL_INT_VEC4 -> 0;
                            case GL_FLOAT_VEC2, GL_FLOAT_VEC3, GL_FLOAT_VEC4 -> 4;
                            case GL_FLOAT_MAT2 -> 8;
                            case GL_FLOAT_MAT3 -> 9;
                            case GL_FLOAT_MAT4 -> 10;
                            default -> -1;
                        };
                        int count = switch (type) {
                            case GL_INT_VEC2, GL_FLOAT_VEC2 -> 2;
                            case GL_INT_VEC3, GL_FLOAT_VEC3 -> 3;
                            case GL_INT_VEC4, GL_FLOAT_VEC4, GL_FLOAT_MAT2 -> 4;
                            case GL_FLOAT_MAT3 -> 9;
                            case GL_FLOAT_MAT4 -> 16;
                            default -> -1;
                        };
                        uniforms.add(new Uniform(name, index + (count > 1 && count <= 4 && index < 8 ? count - 1 : 0), count, (Shader) this));
                    }
                }
            }
        }

        int j = 0;

        for (String string3 : vertexFormat.getElementAttributeNames()) {
            Uniform.glBindAttribLocation(programId, j, string3);
            j++;
        }

        updateLocations();
        markDirty();

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
