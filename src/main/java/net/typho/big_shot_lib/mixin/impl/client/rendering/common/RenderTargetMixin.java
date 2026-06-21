package net.typho.big_shot_lib.mixin.impl.client.rendering.common;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResource;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResourceType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderTarget.class)
public class RenderTargetMixin implements GpuResource {
    @Override
    public @NotNull GpuResourceType getType() {
        return GpuResourceType.FRAMEBUFFER;
    }
}
