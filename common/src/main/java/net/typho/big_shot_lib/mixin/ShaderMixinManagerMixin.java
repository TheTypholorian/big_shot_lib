package net.typho.big_shot_lib.mixin;

import net.typho.big_shot_lib.api.shaders.ShaderSourceKey;
import net.typho.big_shot_lib.api.shaders.mixins.ShaderBytecodeBuffer;
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixin;
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixinManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

@Mixin(value = ShaderMixinManager.class, remap = false)
public class ShaderMixinManagerMixin {
    @Shadow
    private static LinkedList<ShaderMixin.Factory> mixins;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void clinit(CallbackInfo ci) {
        mixins.addFirst(shaderProgramKey -> new ShaderMixin() {
            @Override
            public @NotNull String mixinPreCompile(@NotNull ShaderSourceKey shaderSourceKey, @NotNull String s) {
                return s;
            }

            @Override
            public @NotNull ShaderBytecodeBuffer mixinBytecode(@NotNull ShaderSourceKey shaderSourceKey, @NotNull ShaderBytecodeBuffer shaderBytecodeBuffer) {
                try {
                    byte[] arr = new byte[shaderBytecodeBuffer.buffer.capacity() * 4];
                    MemoryUtil.memByteBuffer(shaderBytecodeBuffer.buffer).get(0, arr, 0, arr.length);
                    Path path = Paths.get("shader_dump", shaderSourceKey.fileName().replaceAll(":", "_") + ".bin");

                    if (!Files.exists(path)) {
                        Files.createFile(path);
                    }

                    Files.write(path, arr);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return shaderBytecodeBuffer;
            }

            @Override
            public @NotNull String mixinPostCompile(@NotNull ShaderSourceKey shaderSourceKey, @NotNull String s) {
                return s;
            }

            @Override
            public @NotNull ShaderMixin then(@NotNull ShaderMixin shaderMixin) {
                return null;
            }
        });
    }
}
