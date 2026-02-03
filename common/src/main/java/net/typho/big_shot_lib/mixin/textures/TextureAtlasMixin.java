package net.typho.big_shot_lib.mixin.textures;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.gl.resource.GlResourceType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin extends AbstractTexture {
    @Shadow
    @Final
    private ResourceLocation location;

    @Inject(
            method = "upload",
            at = @At("TAIL")
    )
    private void upload(SpriteLoader.Preparations preparations, CallbackInfo ci) {
        GlResourceType.TEXTURE_2D.label(getId(), location);
    }
}
