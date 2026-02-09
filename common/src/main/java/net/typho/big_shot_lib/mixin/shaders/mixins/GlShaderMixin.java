package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.gl.shader.GlShader;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderType;
import net.typho.big_shot_lib.api.shaders.ShaderSourceType;
import net.typho.big_shot_lib.impl.shaders.mixins.ShaderMixinThreadLocal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(value = GlShader.class, remap = false)
public class GlShaderMixin {
    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/gl/shader/ShaderWorkarounds;safeShaderSource(ILjava/lang/CharSequence;)V",
                    remap = false
            ),
            index = 1
    )
    private static CharSequence init(
            CharSequence source,
            @Local(argsOnly = true) ShaderType type
    ) {
        return ShaderMixinThreadLocal.INSTANCE.get().apply(
                switch (type) {
                    case ShaderType.VERTEX -> ShaderSourceType.VERTEX;
                    case ShaderType.GEOMETRY -> ShaderSourceType.GEOMETRY;
                    case ShaderType.TESS_CONTROL -> throw new UnsupportedOperationException("Sodium tesselation control shaders are not supported by Big Shot Lib");
                    case ShaderType.TESS_EVALUATION -> throw new UnsupportedOperationException("Sodium tesselation evaluation shaders are not supported by Big Shot Lib");
                    case ShaderType.FRAGMENT -> ShaderSourceType.FRAGMENT;
                },
                source.toString()
        );
    }
}
