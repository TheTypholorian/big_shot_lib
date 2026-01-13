package net.typho.big_shot_lib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.gl.shader.GlShader;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.gl.resource.ShaderType;
import net.typho.big_shot_lib.spirv.ShaderMixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

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
            @Local(argsOnly = true) net.caffeinemc.mods.sodium.client.gl.shader.ShaderType type,
            @Local(argsOnly = true) ResourceLocation location,
            @Local(argsOnly = true) String src
    ) {
        if (ShaderMixinManager.enabled) {
            ShaderType type1 = ShaderType.fromSodiumType(type);
            return ShaderMixinManager.apply(
                    location.withPath(path -> path.substring(0, path.lastIndexOf('.'))),
                    type1,
                    ShaderMixinManager.currentVertexFormat.get(),
                    Objects.requireNonNull(ShaderMixinManager.currentLocationsInfo.get()),
                    location + "." + type1.getExtension(),
                    src,
                    ShaderMixinManager.DEFAULT_ENTRYPOINT
            );
        }

        return source;
    }
}
