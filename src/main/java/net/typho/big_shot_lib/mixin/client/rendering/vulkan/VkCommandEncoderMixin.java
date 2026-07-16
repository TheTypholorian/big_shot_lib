package net.typho.big_shot_lib.mixin.client.rendering.vulkan;

import com.mojang.blaze3d.systems.RenderPassBackend;
import com.mojang.blaze3d.systems.RenderPassDescriptor;
import com.mojang.blaze3d.vulkan.VkCommandEncoder;
import com.mojang.blaze3d.vulkan.VkRenderPass;
import net.typho.big_shot_lib.api.BigShotLib;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VkCommandEncoder.class)
public class VkCommandEncoderMixin {
    @Shadow
    private @Nullable VkRenderPass currentRenderPass;

    @Inject(
            method = "createRenderPass",
            at = @At("HEAD")
    )
    private void createRenderPass(RenderPassDescriptor descriptor, CallbackInfoReturnable<RenderPassBackend> cir) {
        if (currentRenderPass != null) {
            BigShotLib.LOGGER.warn("Creating a render pass '{}' while another pass '{}' is active. This might cause problems.", descriptor.label().get(), currentRenderPass.getLabel().get());
        }
    }
}
