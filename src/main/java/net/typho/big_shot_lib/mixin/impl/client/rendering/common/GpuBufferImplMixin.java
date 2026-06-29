package net.typho.big_shot_lib.mixin.impl.client.rendering.common;

import com.mojang.blaze3d.buffers.GpuBufferImpl;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResource;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResourceType;
import net.typho.big_shot_lib.api.client.rendering.common.Recyclable;
import net.typho.big_shot_lib.api.util.Extension;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;

@Mixin(GpuBufferImpl.class)
public abstract class GpuBufferImplMixin implements GpuBuffer, GpuResource, Recyclable {
    @Shadow
    public abstract void close();

    @Shadow
    public abstract @NonNull GpuBufferSlice slice();

    @Shadow
    public abstract @NonNull GpuBufferSlice slice(long offset, long length);

    @Shadow
    public abstract long size();

    @Override
    public long getSize() {
        return size();
    }

    @Override
    public @NotNull GpuResourceType getType() {
        return GpuResourceType.BUFFER;
    }

    @Override
    public void recycle() {
        close();
    }

    @Override
    public void copyTo(@NotNull GpuBuffer buffer) {
        RenderSystem.getDevice().createCommandEncoder().copyToBuffer(slice(), buffer.slice());
    }

    @Override
    public void upload(@NotNull ByteBuffer data) {
        RenderSystem.getDevice().createCommandEncoder().writeToBuffer(slice(), data);
    }
}
