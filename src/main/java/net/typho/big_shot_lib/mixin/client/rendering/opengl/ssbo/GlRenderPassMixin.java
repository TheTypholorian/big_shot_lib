package net.typho.big_shot_lib.mixin.client.rendering.opengl.ssbo;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.buffers.GpuBufferImpl;
import net.typho.big_shot_lib.client.api.ext.RenderPassExtension;
import net.typho.big_shot_lib.client.api.rendering.common.GpuBuffer;
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuIndexType;
import net.typho.big_shot_lib.api.util.Extension;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "com/mojang/blaze3d/opengl/GlRenderPass")
public abstract class GlRenderPassMixin implements RenderPassExtension {
    @Shadow
    public abstract void setUniform(String p_410717_, GpuBufferImpl p_418484_);
    @Shadow
    public abstract void setIndexBuffer(@Nullable GpuBufferImpl p_410828_, IndexType p_410040_);

    @Override
    public void setUniform(@NotNull String name, @NotNull GpuBuffer buffer) {
        setUniform(name, Extension.castTo(buffer));
    }

    @Override
    public void setIndexBuffer(@NotNull GpuBuffer buffer, @NotNull GpuIndexType type) {
        setIndexBuffer(Extension.castTo(buffer), Extension.castTo(type));
    }
}
