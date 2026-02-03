package net.typho.big_shot_lib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.CompiledShader;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.gl.resource.ShaderType;
import net.typho.big_shot_lib.spirv.ShaderMixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(CompiledShader.class)
public class CompiledShaderMixin {
    @ModifyArg(
            method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;glShaderSource(ILjava/lang/String;)V",
                    remap = false
            ),
            index = 1
    )
    private static String compile(
            String code,
            @Local(argsOnly = true) ResourceLocation shaderId,
            @Local(argsOnly = true) CompiledShader.Type type
    ) {
        if (ShaderMixinManager.enabled) {
            return ShaderMixinManager.apply(
                    shaderId,
                    ShaderType.fromVanillaType(type),
                    ShaderMixinManager.currentVertexFormat.get(),
                    Objects.requireNonNull(ShaderMixinManager.currentLocationsInfo.get()),
                    shaderId + (type == CompiledShader.Type.VERTEX ? ".vsh" : ".fsh"),
                    code,
                    ShaderMixinManager.DEFAULT_ENTRYPOINT
            );
        }

        return code;
    }
}
