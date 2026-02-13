package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import kotlin.collections.CollectionsKt;
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType;
import net.typho.big_shot_lib.impl.shaders.mixins.ShaderMixinThreadLocal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

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
        return CollectionsKt.listOf(
                ShaderMixinThreadLocal.INSTANCE.get().apply(
                        switch (type) {
                            case VERTEX -> ShaderSourceType.VERTEX;
                            case FRAGMENT -> ShaderSourceType.FRAGMENT;
                        },
                        String.join("\n", list)
                )
        );
    }
}
