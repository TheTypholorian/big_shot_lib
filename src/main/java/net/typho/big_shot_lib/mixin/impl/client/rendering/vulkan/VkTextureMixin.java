package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan;

import com.mojang.blaze3d.vulkan.VkTexture;
import net.typho.big_shot_lib.api.client.rendering.vulkan.VkNamed;
import net.typho.big_shot_lib.api.client.rendering.vulkan.VkResource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VkTexture.class)
public abstract class VkTextureMixin implements VkResource.VMA, VkNamed {
    @Shadow
    @Final
    private long vmaAllocation;

    @Shadow
    public abstract long vkImage();

    @Override
    public long getVkId() {
        return vkImage();
    }

    @Override
    public long getVmaPtr() {
        return vmaAllocation;
    }
}
