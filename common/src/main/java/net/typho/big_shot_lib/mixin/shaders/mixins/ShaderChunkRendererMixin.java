package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.caffeinemc.mods.sodium.client.gl.shader.GlProgram;
import net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.typho.big_shot_lib.shaders.mixins.ShaderLocationsInfo;
import net.typho.big_shot_lib.shaders.mixins.ShaderMixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = ShaderChunkRenderer.class, remap = false)
public class ShaderChunkRendererMixin {
    @Inject(
            method = "createShader",
            at = @At("HEAD")
    )
    private void createShader1(String path, ChunkShaderOptions options, CallbackInfoReturnable<GlProgram<ChunkShaderInterface>> cir) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.set(DefaultVertexFormat.POSITION_TEX_COLOR);
            ShaderMixinManager.currentLocationsInfo.set(new ShaderLocationsInfo(false));
        }
    }

    @Inject(
            method = "createShader",
            at = @At("TAIL")
    )
    private void createShader2(String path, ChunkShaderOptions options, CallbackInfoReturnable<GlProgram<ChunkShaderInterface>> cir) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.remove();
            ShaderMixinManager.currentLocationsInfo.remove();
        }
    }
}
