package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.CompiledShader;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType;
import net.typho.big_shot_lib.impl.shaders.mixins.ShaderMixinThreadLocal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
            @Local(argsOnly = true) CompiledShader.Type type
    ) {
        return ShaderMixinThreadLocal.INSTANCE.get().apply(
                switch (type) {
                    case VERTEX -> ShaderSourceType.VERTEX;
                    case FRAGMENT -> ShaderSourceType.FRAGMENT;
                },
                code
        );
    }
}
