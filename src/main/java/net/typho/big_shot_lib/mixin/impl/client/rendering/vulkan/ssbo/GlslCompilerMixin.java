package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan.ssbo;

import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vulkan.VkBindGroupLayout;
import com.mojang.blaze3d.vulkan.glsl.GlslCompiler;
import com.mojang.blaze3d.vulkan.glsl.IntermediaryShaderModule;
import com.mojang.blaze3d.vulkan.glsl.ShaderCompileException;
import net.typho.big_shot_lib.impl.client.rendering.vulkan.VkStorageBufferBindGroupEntryType;
import net.typho.big_shot_lib.impl.client.rendering.vulkan.ssbo.IntermediaryShaderModuleExtension;
import net.typho.big_shot_lib.impl.client.rendering.vulkan.ssbo.SpvStorageBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(GlslCompiler.class)
public class GlslCompilerMixin {
    @Inject(
            method = "addToBindGroup",
            at = @At("HEAD")
    )
    private static void addToBindGroup(
            List<VkBindGroupLayout.Entry> entries,
            IntermediaryShaderModule shader,
            RenderPipeline pipeline,
            CallbackInfo ci
    ) throws ShaderCompileException {
        for (SpvStorageBuffer buffer : ((IntermediaryShaderModuleExtension) (Object) shader).getBig_shot_lib$storageBuffers()) {
            Optional<BindGroupLayout.UniformDescription> uniformDescription = BindGroupLayout.flattenUniforms(pipeline.getBindGroupLayouts())
                    .stream()
                    .filter(d -> d.name().equals(buffer.name))
                    .findFirst();
            if (uniformDescription.isEmpty()) {
                throw new ShaderCompileException("Unable to find shader defined storage buffer (" + buffer.name + ")");
            }

            if (entries.stream().noneMatch(e -> e.type() == VkStorageBufferBindGroupEntryType.INSTANCE && e.name().equals(buffer.name))) {
                entries.add(new VkBindGroupLayout.Entry(VkStorageBufferBindGroupEntryType.INSTANCE, buffer.name, null));
            }
        }
    }
}
