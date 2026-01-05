package net.typho.big_shot_lib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.gl.shader.GlShader;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.gl.resource.ShaderType;
import net.typho.big_shot_lib.spirv.ShaderMixinCallback;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.ByteBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.ARBGLSPIRV.GL_SHADER_BINARY_FORMAT_SPIR_V_ARB;
import static org.lwjgl.opengl.ARBGLSPIRV.glSpecializeShaderARB;
import static org.lwjgl.opengl.GL41.glShaderBinary;

@Pseudo
@Mixin(value = GlShader.class, remap = false)
public class GlShaderMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/gl/shader/ShaderWorkarounds;safeShaderSource(ILjava/lang/CharSequence;)V",
                    remap = false
            )
    )
    private static void init(
            int sourceBuffer,
            CharSequence pointers,
            Operation<Void> original,
            @Local(argsOnly = true) net.caffeinemc.mods.sodium.client.gl.shader.ShaderType type,
            @Local(argsOnly = true) ResourceLocation location,
            @Local(argsOnly = true) String src,
            @Local int handle
    ) {
        if (ShaderMixinCallback.enabled) {
            ShaderType type1 = ShaderType.fromSodiumType(type);
            ByteBuffer compiled = ShaderMixinCallback.compile(
                    location.withPath(path -> path.substring(0, path.lastIndexOf('.'))),
                    type1,
                    ShaderMixinCallback.currentVertexFormat.get(),
                    Objects.requireNonNull(ShaderMixinCallback.currentLocationsInfo.get()),
                    location + "." + type1.getExtension(),
                    src,
                    "main"
            );
            glShaderBinary(new int[]{handle}, GL_SHADER_BINARY_FORMAT_SPIR_V_ARB, compiled);
            glSpecializeShaderARB(handle, "main", new int[0], new int[0]);
            MemoryUtil.memFree(compiled);
        } else {
            original.call(sourceBuffer, pointers);
        }
    }

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL20C;glCompileShader(I)V",
                    remap = false
            )
    )
    private static void init(int i, Operation<Void> original) {
        if (!ShaderMixinCallback.enabled) {
            original.call(i);
        }
    }
}
