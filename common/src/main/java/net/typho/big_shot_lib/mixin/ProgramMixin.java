package net.typho.big_shot_lib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.gl.resource.ShaderType;
import net.typho.big_shot_lib.spirv.ShaderMixinCallback;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.ARBGLSPIRV.GL_SHADER_BINARY_FORMAT_SPIR_V_ARB;
import static org.lwjgl.opengl.ARBGLSPIRV.glSpecializeShaderARB;
import static org.lwjgl.opengl.GL41.glShaderBinary;

@Mixin(Program.class)
public class ProgramMixin {
    @WrapOperation(
            method = "compileShaderInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;glShaderSource(ILjava/util/List;)V",
                    remap = false
            )
    )
    private static void compileShaderInternal(
            int id,
            List<String> list,
            Operation<Void> original,
            @Local(argsOnly = true) Program.Type type,
            @Local(argsOnly = true, ordinal = 0) String name
    ) {
        if (ShaderMixinCallback.enabled) {
            ByteBuffer compiled = ShaderMixinCallback.compile(
                    ResourceLocation.parse(name),
                    ShaderType.fromVanillaType(type),
                    ShaderMixinCallback.currentVertexFormat.get(),
                    Objects.requireNonNull(ShaderMixinCallback.currentLocationsInfo.get()),
                    name + type.getExtension(),
                    String.join("", list),
                    "main"
            );
            glShaderBinary(new int[]{id}, GL_SHADER_BINARY_FORMAT_SPIR_V_ARB, compiled);
            glSpecializeShaderARB(id, "main", new int[0], new int[0]);
            MemoryUtil.memFree(compiled);
        } else {
            original.call(id, list);
        }
    }

    @WrapOperation(
            method = "compileShaderInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;glCompileShader(I)V",
                    remap = false
            )
    )
    private static void compileShaderInternal(int shader, Operation<Void> original) {
        if (!ShaderMixinCallback.enabled) {
            original.call(shader);
        }
    }
}
