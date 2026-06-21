package net.typho.big_shot_lib.mixin.impl.client.rendering.common;

import com.mojang.blaze3d.buffers.GpuBuffer;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResource;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResourceType;
import net.typho.big_shot_lib.api.client.rendering.common.Recyclable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GpuBuffer.class)
public abstract class GpuBufferMixin implements GpuResource, Recyclable {
    @Shadow
    public abstract void close();

    @Override
    public @NotNull GpuResourceType getType() {
        return GpuResourceType.BUFFER;
    }

    @Override
    public void recycle() {
        close();
    }
}
