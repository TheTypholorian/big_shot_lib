package net.typho.big_shot_lib.mixin.buffers;

import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.typho.big_shot_lib.BigShotLib;
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBufferRegistry;
import net.typho.big_shot_lib.api.client.rendering.state.GlStateStack;
import net.typho.big_shot_lib.impl.buffers.DynamicBufferRegistryImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GlStateManager.class, remap = false)
public class GlStateManagerMixin {
    @Inject(
            method = "_enableBlend",
            at = @At("TAIL")
    )
    private static void enableBlend(CallbackInfo ci) {
        if (GlStateStack.framebuffer.query.get() == BigShotLib.glId(Minecraft.getInstance().getMainRenderTarget())) {
            ((DynamicBufferRegistryImpl) DynamicBufferRegistry.INSTANCE).enableBlend();
        }
    }

    @Inject(
            method = "_disableBlend",
            at = @At("TAIL")
    )
    private static void disableBlend(CallbackInfo ci) {
        if (GlStateStack.framebuffer.query.get() == BigShotLib.glId(Minecraft.getInstance().getMainRenderTarget())) {
            ((DynamicBufferRegistryImpl) DynamicBufferRegistry.INSTANCE).disableBlend();
        }
    }
}
