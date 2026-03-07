package net.typho.big_shot_lib.mixin.util;

import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.typho.big_shot_lib.api.client.util.panoramas.PanoramaTexture;
import net.typho.big_shot_lib.impl.util.PanoramaTextureStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CubeMap.class)
public class CubeMapMixin implements PanoramaTextureStorage {
    @Unique
    private PanoramaTexture big_shot_lib$panorama_texture;

    @Override
    public @Nullable PanoramaTexture getBig_shot_lib$panorama_texture() {
        return big_shot_lib$panorama_texture;
    }

    @Override
    public void setBig_shot_lib$panorama_texture(@Nullable PanoramaTexture panoramaTexture) {
        big_shot_lib$panorama_texture = panoramaTexture;
    }

    @ModifyArg(
            method = "registerTextures",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;register(Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/renderer/texture/AbstractTexture;)V"
            ),
            index = 1
    )
    private AbstractTexture registerTextures(AbstractTexture texture) {
        ((PanoramaTextureStorage) texture).setBig_shot_lib$panorama_texture(big_shot_lib$panorama_texture);
        return texture;
    }
}
