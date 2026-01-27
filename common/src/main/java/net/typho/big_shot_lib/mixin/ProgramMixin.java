package net.typho.big_shot_lib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import kotlin.collections.CollectionsKt;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.gl.resource.ShaderType;
import net.typho.big_shot_lib.shaders.mixins.ShaderMixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.Objects;

@Mixin(Program.class)
public class ProgramMixin {
    @ModifyArg(
            method = "compileShaderInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;glShaderSource(ILjava/util/List;)V",
                    remap = false
            ),
            index = 1
    )
    private static List<String> compileShaderInternal(
            List<String> list,
            @Local(argsOnly = true) Program.Type type,
            @Local(argsOnly = true, ordinal = 0) String name
    ) {
        if (ShaderMixinManager.enabled) {
            return CollectionsKt.listOf(ShaderMixinManager.apply(
                    ResourceLocation.parse(name),
                    ShaderType.fromVanillaType(type),
                    ShaderMixinManager.currentVertexFormat.get(),
                    Objects.requireNonNull(ShaderMixinManager.currentLocationsInfo.get()),
                    name + type.getExtension(),
                    String.join("", list),
                    ShaderMixinManager.DEFAULT_ENTRYPOINT
            ));
        }

        return list;
    }
}
