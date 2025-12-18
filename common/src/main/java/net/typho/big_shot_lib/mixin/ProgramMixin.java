package net.typho.big_shot_lib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.gl.resource.ShaderType;
import net.typho.big_shot_lib.spirv.ShaderMixinCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.ARBGLSPIRV.GL_SHADER_BINARY_FORMAT_SPIR_V_ARB;
import static org.lwjgl.opengl.ARBGLSPIRV.glSpecializeShaderARB;
import static org.lwjgl.opengl.GL41.glShaderBinary;

@Mixin(value = Program.class, remap = false)
public class ProgramMixin {
    @Redirect(
            method = "compileShaderInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;glShaderSource(ILjava/util/List;)V"
            )
    )
    private static void compileShaderInternal(
            int id,
            List<String> list,
            @Local(argsOnly = true) Program.Type type,
            @Local(argsOnly = true, ordinal = 0) String name
    ) {
        String code = String.join("", list);

        if (code.startsWith("#version")) {
            int version = Integer.parseInt(code.split("\\s+")[1]);

            if (version < 430) {
                code = "#version 430 core" + code.substring(code.indexOf('\n'));
            }
        } else {
            code = "#version 430 core\n" + code;
        }

        ShaderType shaderType = ShaderType.fromVanillaType(type);
        ByteBuffer compiled = ShaderMixinCallback.invoke(
                ResourceLocation.withDefaultNamespace(name),
                shaderType,
                ShaderMixinCallback.compile(
                        shaderType,
                        name + type.getExtension(),
                        code,
                        "main"
                )
        );
        glShaderBinary(new int[]{id}, GL_SHADER_BINARY_FORMAT_SPIR_V_ARB, compiled);
        glSpecializeShaderARB(id, "main", new int[0], new int[0]);
    }

    @Redirect(
            method = "compileShaderInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;glCompileShader(I)V"
            )
    )
    private static void compileShaderInternal(int shader) {
    }
}
