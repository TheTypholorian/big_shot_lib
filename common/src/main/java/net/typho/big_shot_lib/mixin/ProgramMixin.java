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
import java.util.LinkedList;
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

        List<String> lines = new LinkedList<>();
        int input = 0, output = 0, uniform = 0, binding = 0;

        for (String line : code.split("\n")) {
            String[] tokens = line.trim().split("\\s+");

            if (tokens.length > 0) {
                String token = tokens[0];
                int i = 0;

                if (token.equals("flat") && tokens.length >= 2) {
                    token = tokens[1];
                    i = 1;
                }

                switch (token) {
                    case "in": {
                        line = "layout(location = " + (input++) + ") " + line;
                        break;
                    }
                    case "out": {
                        line = "layout(location = " + (output++) + ") " + line;
                        break;
                    }
                    case "uniform": {
                        String uniformType = tokens[i + 1];

                        if (uniformType.startsWith("sampler") || uniformType.startsWith("image") || uniformType.equals("atomic_uint")) {
                            line = "layout(binding = " + (binding++) + ") " + line;
                        } else {
                            line = "layout(location = " + (uniform++) + ") " + line;
                        }
                    }
                }
            }

            lines.add(line);
        }

        String tweakedCode = String.join("\n", lines);

        System.out.println(tweakedCode);

        ShaderType shaderType = ShaderType.fromVanillaType(type);
        ByteBuffer compiled = ShaderMixinCallback.invoke(
                ResourceLocation.withDefaultNamespace(name),
                shaderType,
                ShaderMixinCallback.compile(
                        shaderType,
                        name + type.getExtension(),
                        tweakedCode,
                        "main"
                )
        );
        glShaderBinary(new int[]{id}, GL_SHADER_BINARY_FORMAT_SPIR_V_ARB, compiled);
        glSpecializeShaderARB(id, "main", new int[0], new int[0]);
    }
}
