package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan.ssbo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vulkan.VkConst;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBufferUsage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static org.lwjgl.vulkan.VK10.VK_BUFFER_USAGE_STORAGE_BUFFER_BIT;

@Mixin(VkConst.class)
public class VkConstMixin {
    @ModifyReturnValue(
            method = "bufferUsageToVk",
            at = @At("RETURN")
    )
    private static int bufferUsageToVk(int vk, @Local(argsOnly = true) int usage) {
        if ((usage & GpuBufferUsage.shaderStorage()) != 0) {
            vk |= VK_BUFFER_USAGE_STORAGE_BUFFER_BIT;
        }

        return vk;
    }
}
