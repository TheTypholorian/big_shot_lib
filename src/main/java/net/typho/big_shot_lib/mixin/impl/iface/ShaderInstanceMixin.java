package net.typho.big_shot_lib.mixin.impl.iface;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.BigShotLib;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.*;
import net.typho.big_shot_lib.api.client.rendering.state.TextureBinding;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat;
import net.typho.big_shot_lib.api.util.ImmutableExtensionKt;
import net.typho.big_shot_lib.impl.client.rendering.opengl.ShaderInstanceExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL20.*;

@Mixin(ShaderInstance.class)
public abstract class ShaderInstanceMixin implements GlProgram, ShaderInstanceExtension {
    @Shadow
    @Final
    @Mutable
    private int programId;
    @Shadow
    @Final
    @Mutable
    private String name;
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
    private List<Uniform> uniforms;
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
    private Map<String, Object> samplerMap;
    @Shadow
    @Final
    @Mutable
    private List<String> samplerNames;
    @Shadow
    @Final
    @Mutable
    private List<Integer> samplerLocations;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform MODEL_VIEW_MATRIX;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform PROJECTION_MATRIX;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform TEXTURE_MATRIX;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform SCREEN_SIZE;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform COLOR_MODULATOR;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform LIGHT0_DIRECTION;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform LIGHT1_DIRECTION;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform GLINT_ALPHA;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform FOG_START;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform FOG_END;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform FOG_COLOR;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform FOG_SHAPE;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform LINE_WIDTH;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform GAME_TIME;
    @Shadow
    @Final
    @Nullable
    @Mutable
    public Uniform CHUNK_OFFSET;

    @Shadow
    public abstract void close();

    @Shadow
    @Nullable
    public abstract Uniform getUniform(String name);

    @Shadow
    public abstract void setSampler(String name, Object textureId);

    @Shadow
    public abstract void markDirty();

    @Shadow
    protected abstract void updateLocations();

    @Unique
    private boolean freed = false;

    @Override
    public @NotNull Identifier getLocation() {
        return Identifier.parse(name);
    }

    @Override
    public int getGlId() {
        return programId;
    }

    @Override
    public boolean getFreed() {
        return freed;
    }

    @Override
    public @NotNull GlResourceType getType() {
        return GlResourceType.PROGRAM;
    }

    @Override
    public boolean validate() {
        glValidateProgram(getGlId());

        return glGetProgrami(getGlId(), GL_VALIDATE_STATUS) == GL_TRUE;
    }

    @Override
    public boolean link() {
        glLinkProgram(getGlId());

        boolean success = glGetProgrami(getGlId(), GL_LINK_STATUS) == GL_TRUE;

        if (success) {
            big_shot_lib$initUniforms();
        }

        return success;
    }

    @Override
    @NotNull
    public String getInfoLog() {
        return glGetProgramInfoLog(getGlId(), 4096).trim();
    }

    @Override
    public void detach(@NotNull GlShader shader) {
        glDetachShader(getGlId(), shader.getGlId());
    }

    @Override
    public void attach(@NotNull GlShader shader) {
        glAttachShader(getGlId(), shader.getGlId());

        switch (shader.getShaderType()) {
            case VERTEX -> vertexProgram = ImmutableExtensionKt.getExtensionValue(shader, Program.class);
            case FRAGMENT -> fragmentProgram = ImmutableExtensionKt.getExtensionValue(shader, Program.class);
        }
    }

    @Override
    public void setUniform(String name, Consumer<GlUniform> value) {
        Uniform uniform = getUniform(name);

        if (uniform != null) {
            value.accept(uniform);
        }
    }

    @Override
    public void setTexture(int index, TextureBinding binding) {
        // TODO set filters
        setSampler("Sampler" + index, binding);
    }

    @Override
    public @NotNull NeoVertexFormat getFormat() {
        return ImmutableExtensionKt.getExtensionValue(vertexFormat, NeoVertexFormat.class);
    }

    @Override
    public void big_shot_lib$init(@NotNull Identifier location, @NotNull VertexFormat format, int glId) {
        samplerMap = Maps.newHashMap();
        samplerNames = Lists.newArrayList();
        samplerLocations = Lists.newArrayList();
        uniforms = Lists.newArrayList();
        uniformLocations = Lists.newArrayList();
        uniformMap = Maps.newHashMap();
        name = location.toShortString();
        vertexFormat = format;
        programId = glId;
    }

    @Override
    public void big_shot_lib$initUniforms() {
        int index = 0;

        for (String element : vertexFormat.getElementAttributeNames()) {
            glBindAttribLocation(programId, index, element);
            index++;
        }

        samplerMap.clear();
        samplerNames.clear();
        samplerLocations.clear();
        uniforms.clear();
        uniformLocations.clear();
        uniformMap.clear();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer numUniforms = stack.mallocInt(1);
            glGetProgramiv(programId, GL_ACTIVE_UNIFORMS, numUniforms);

            IntBuffer size = stack.mallocInt(1);
            IntBuffer type = stack.mallocInt(1);

            for (int i = 0; i < numUniforms.get(0); i++) {
                String name = glGetActiveUniform(
                        programId,
                        i,
                        256,
                        size,
                        type
                );

                int type1 = type.get(0);

                if (type1 >= GL_SAMPLER_1D && type1 <= GL_SAMPLER_2D_SHADOW) {
                    samplerMap.put(name, null);
                    samplerNames.add(name);
                } else {
                    int mojType = switch (type1) {
                        case GL_INT -> Uniform.UT_INT1;
                        case GL_INT_VEC2 -> Uniform.UT_INT2;
                        case GL_INT_VEC3 -> Uniform.UT_INT3;
                        case GL_INT_VEC4 -> Uniform.UT_INT4;
                        case GL_FLOAT -> Uniform.UT_FLOAT1;
                        case GL_FLOAT_VEC2 -> Uniform.UT_FLOAT2;
                        case GL_FLOAT_VEC3 -> Uniform.UT_FLOAT3;
                        case GL_FLOAT_VEC4 -> Uniform.UT_FLOAT4;
                        case GL_FLOAT_MAT2 -> Uniform.UT_MAT2;
                        case GL_FLOAT_MAT3 -> Uniform.UT_MAT3;
                        case GL_FLOAT_MAT4 -> Uniform.UT_MAT4;
                        default -> throw new UnsupportedOperationException(name + " is an unsupported uniform type 0x" + Integer.toHexString(type1) + " in shader program " + name);
                    };
                    int mojCount = switch (type1) {
                        case GL_INT, GL_FLOAT -> 1;
                        case GL_INT_VEC2, GL_FLOAT_VEC2 -> 2;
                        case GL_INT_VEC3, GL_FLOAT_VEC3 -> 3;
                        case GL_INT_VEC4, GL_FLOAT_VEC4, GL_FLOAT_MAT2 -> 4;
                        case GL_FLOAT_MAT3 -> 9;
                        case GL_FLOAT_MAT4 -> 16;
                        default -> throw new UnsupportedOperationException(name + " is an unsupported uniform type 0x" + Integer.toHexString(type1) + " in shader program " + name);
                    };

                    uniforms.add(new Uniform(name, mojType, mojCount, (Shader) this));
                }
            }
        }

        updateLocations();
        markDirty();
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

    @WrapMethod(
            method = "close"
    )
    private void close(Operation<Void> original) {
        if (!freed) {
            original.call();
            freed = true;
        }
    }

    @Inject(
            method = "apply",
            at = @At(
                    value = "CONSTANT",
                    args = "classValue=com/mojang/blaze3d/pipeline/RenderTarget"
            )
    )
    private void apply(CallbackInfo ci, @Local Object value, @Local(ordinal = 3) LocalIntRef textureId) {
        if (value instanceof TextureBinding binding) {
            var texture = binding.getTexture();
            texture.setBlur(binding.getBlur()); // TODO 1.21.11+ samplers
            texture.setMipmap(binding.getMipmap());
            textureId.set(texture.getGlId());
        }
    }
}
