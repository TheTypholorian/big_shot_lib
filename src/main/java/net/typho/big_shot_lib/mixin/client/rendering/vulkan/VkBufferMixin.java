package net.typho.big_shot_lib.mixin.client.rendering.vulkan;

import com.mojang.blaze3d.vulkan.VkBuffer;
import net.typho.big_shot_lib.client.api.rendering.common.GpuResource;
import net.typho.big_shot_lib.client.api.rendering.vulkan.VkNamed;
import net.typho.big_shot_lib.client.api.rendering.vulkan.VkResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VkBuffer.class)
public abstract class VkBufferMixin implements VkResource, GpuResource, VkNamed {
    @Shadow
    public abstract long vkBuffer();

    @Override
    public long getVkId() {
        return vkBuffer();
    }
}
