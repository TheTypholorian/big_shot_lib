package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan;

import com.mojang.blaze3d.vulkan.VkBuffer;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResource;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResourceType;
import net.typho.big_shot_lib.api.client.rendering.vulkan.VkNamed;
import net.typho.big_shot_lib.api.client.rendering.vulkan.VkResource;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VkBuffer.class)
public abstract class VkBufferMixin implements VkResource, GpuResource, VkNamed {
    @Shadow
    public abstract long vkBuffer();

    @Override
    public @NotNull GpuResourceType getType() {
        return GpuResourceType.BUFFER;
    }

    @Override
    public long getVkId() {
        return vkBuffer();
    }
}
