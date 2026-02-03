package net.typho.big_shot_lib.mixin.textures;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.gl.resource.GlResourceType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleTexture.class)
public abstract class SimpleTextureMixin extends AbstractTexture {
    @Shadow
    @Final
    protected ResourceLocation location;

    @Inject(
            method = "doLoad",
            at = @At("TAIL")
    )
    private void doLoad(NativeImage image, boolean blur, boolean clamp, CallbackInfo ci) {
        GlResourceType.TEXTURE_2D.label(getId(), location);
    }
}
