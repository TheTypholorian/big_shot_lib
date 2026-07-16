package net.typho.big_shot_lib.mixin.client.rendering.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.shaders.ShaderType;
import kotlin.collections.MapsKt;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.typho.big_shot_lib.client.api.rendering.NeoShaderPreprocessor;
import net.typho.big_shot_lib.api.util.Extension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ShaderManager.class)
public class ShaderManagerMixin {
    @Shadow
    private static boolean isShader(Identifier location) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/client/renderer/ShaderManager$Configs;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;entrySet()Ljava/util/Set;"
            )
    )
    private static void prepare(
            ResourceManager manager,
            ProfilerFiller profiler,
            CallbackInfoReturnable<ShaderManager.Configs> cir,
            @Local LocalRef<Map<Identifier, Resource>> files
    ) {
        Map<Identifier, Resource> newFiles = MapsKt.toMutableMap(files.get());
        newFiles.putAll(manager.listResources("neo/shaders", ShaderManagerMixin::isShader));
        files.set(newFiles);
    }

    @WrapOperation(
            method = "loadShader",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/shaders/ShaderType;idConverter()Lnet/minecraft/resources/FileToIdConverter;"
            )
    )
    private static FileToIdConverter loadShader(
            ShaderType instance,
            Operation<FileToIdConverter> original,
            @Local(argsOnly = true) Identifier location
    ) {
        FileToIdConverter converter = original.call(instance);

        if (location.getPath().startsWith("neo/shaders")) {
            return new FileToIdConverter("neo/shaders", converter.extension());
        } else {
            return converter;
        }
    }

    @ModifyArg(
            method = "loadShader",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/preprocessor/GlslPreprocessor;process(Ljava/lang/String;)Ljava/util/List;"
            )
    )
    private static String loadShader(
            String code,
            @Local(argsOnly = true) Identifier location,
            @Local(ordinal = 1) Identifier id,
            @Local(argsOnly = true) ShaderType type,
            @Local(argsOnly = true) Map<Identifier, Resource> files
    ) {
        //if (location.getPath().startsWith("neo/shaders")) {
            for (NeoShaderPreprocessor preprocessor : NeoShaderPreprocessor.REGISTRY) {
                code = preprocessor.apply(id, Extension.castTo(type), code, files);
            }
        //}

        return code;
    }
}
