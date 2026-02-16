package net.typho.big_shot_lib.mixin.util;

import com.mojang.blaze3d.pipeline.MainTarget;
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebufferAttachment;
import net.typho.big_shot_lib.api.client.rendering.textures.GlTexture;
import net.typho.big_shot_lib.impl.util.RenderTargetExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MainTarget.class)
public abstract class MainTargetMixin {
    @Redirect(
            method = "allocateAttachments",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/TextureUtil;generateTextureId()I",
                    remap = false,
                    ordinal = 0
            )
    )
    private int allocateAttachments1(int width, int height) {
        return ((GlTexture) ((RenderTargetExtension) this).big_shot_lib$getColorAttachments().getFirst()).glId();
    }

    @Redirect(
            method = "allocateAttachments",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/TextureUtil;generateTextureId()I",
                    remap = false,
                    ordinal = 1
            )
    )
    private int allocateAttachments2(int width, int height) {
        GlFramebufferAttachment depth = ((RenderTargetExtension) this).big_shot_lib$getDepthAttachment();

        if (depth == null) {
            return -1;
        }

        return ((GlTexture) depth).glId();
    }
}
