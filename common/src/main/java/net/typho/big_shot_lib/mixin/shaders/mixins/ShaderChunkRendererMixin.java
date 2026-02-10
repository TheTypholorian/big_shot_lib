package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import kotlin.collections.CollectionsKt;
import net.caffeinemc.mods.sodium.client.gl.attribute.GlVertexAttribute;
import net.caffeinemc.mods.sodium.client.gl.attribute.GlVertexFormat;
import net.caffeinemc.mods.sodium.client.gl.shader.GlProgram;
import net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.vertex.VertexFormatAttribute;
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
import java.util.Map;

@Pseudo
@Mixin(value = ShaderChunkRenderer.class, remap = false)
public class ShaderChunkRendererMixin {
    @Inject(
            method = "createShader",
            at = @At("HEAD")
    )
    private void createShader1(String path, ChunkShaderOptions options, CallbackInfoReturnable<GlProgram<ChunkShaderInterface>> cir) {
        GlVertexFormat glFormat = options.vertexType().getVertexFormat();
        VertexFormat.Builder formatBuilder = VertexFormat.builder();

        for (Map.Entry<VertexFormatAttribute, GlVertexAttribute> entry : ((GlVertexFormatAccessor) glFormat).getAttributes().entrySet()) {
            String name = switch (entry.getKey().name().toUpperCase()) {
                case "POSITION" -> "a_Position";
                case "COLOR" -> "a_Color";
                case "TEXTURE" -> "a_TexCoord";
                case "LIGHT_MATERIAL_INDEX" -> "a_LightAndData";
                case "NORMAL" -> "a_Normal";
                default -> "a_" + Character.toUpperCase(entry.getKey().name().charAt(0)) + entry.getKey().name().substring(1).toLowerCase();
            };
            formatBuilder.add(name, switch (entry.getKey().name().toUpperCase()) {
                case "POSITION" -> VertexFormatElement.POSITION;
                case "COLOR" -> VertexFormatElement.COLOR;
                case "TEXTURE", "TEX_COORD", "TEXCOORD", "UV", "UV0", "UV_0" -> VertexFormatElement.UV0;
                case "OVERLAY", "UV1", "UV_1" -> VertexFormatElement.UV1;
                case "LIGHT", "LIGHT_MATERIAL_INDEX", "UV2", "UV_2" -> VertexFormatElement.UV2;
                case "NORMAL" -> VertexFormatElement.NORMAL;
                default -> throw new UnsupportedOperationException(entry.getKey().name());
            });
        }

        ShaderMixinThreadLocal.push(new ShaderProgramKey(
                ShaderLoaderType.SODIUM,
                ResourceLocation.fromNamespaceAndPath("sodium", path),
                formatBuilder.build(),
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
