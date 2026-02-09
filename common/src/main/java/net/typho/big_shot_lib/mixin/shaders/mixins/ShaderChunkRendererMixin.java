package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import kotlin.collections.CollectionsKt;
import net.caffeinemc.mods.sodium.client.gl.shader.GlProgram;
import net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.api.shaders.ShaderLoaderType;
import net.typho.big_shot_lib.api.shaders.ShaderProgramKey;
import net.typho.big_shot_lib.api.shaders.ShaderSourceType;
import net.typho.big_shot_lib.impl.shaders.mixins.ShaderMixinThreadLocal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;

@Pseudo
@Mixin(value = ShaderChunkRenderer.class, remap = false)
public class ShaderChunkRendererMixin {
    @Inject(
            method = "createShader",
            at = @At("HEAD")
    )
    private void createShader1(String path, ChunkShaderOptions options, CallbackInfoReturnable<GlProgram<ChunkShaderInterface>> cir) {
        ShaderMixinThreadLocal.push(new ShaderProgramKey(
                ShaderLoaderType.SODIUM,
                ResourceLocation.fromNamespaceAndPath("sodium", path),
                DefaultVertexFormat.POSITION_TEX_COLOR,
                new HashSet<>(CollectionsKt.listOf(ShaderSourceType.VERTEX, ShaderSourceType.FRAGMENT))
        ));
    }

    @Inject(
            method = "createShader",
            at = @At("TAIL")
    )
    private void createShader2(String path, ChunkShaderOptions options, CallbackInfoReturnable<GlProgram<ChunkShaderInterface>> cir) {
        ShaderMixinThreadLocal.pop();
    }
}
