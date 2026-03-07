package net.typho.big_shot_lib.mixin.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.texture.CubeMapTexture;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.BigShotLib;
import net.typho.big_shot_lib.api.client.util.panoramas.PanoramaTexture;
import net.typho.big_shot_lib.impl.util.PanoramaTextureStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CubeMapTexture.class)
public class CubeMapTextureMixin implements PanoramaTextureStorage {
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

    @WrapOperation(
            method = "loadContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceLocation;withSuffix(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 0
            )
    )
    private ResourceLocation loadContents1(ResourceLocation instance, String pathSuffix, Operation<ResourceLocation> original) {
        return big_shot_lib$panorama_texture == null ? original.call(instance, pathSuffix) : BigShotLib.toMojang(big_shot_lib$panorama_texture.east);
    }

    @WrapOperation(
            method = "loadContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceLocation;withSuffix(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 1
            )
    )
    private ResourceLocation loadContents2(
            ResourceLocation instance,
            String pathSuffix,
            Operation<ResourceLocation> original,
            @Local(ordinal = 2) int k
    ) {
        return big_shot_lib$panorama_texture == null ? original.call(instance, pathSuffix) : BigShotLib.toMojang(switch (k) {
            case 0 -> big_shot_lib$panorama_texture.east;
            case 1 -> big_shot_lib$panorama_texture.west;
            case 2 -> big_shot_lib$panorama_texture.down;
            case 3 -> big_shot_lib$panorama_texture.up;
            case 4 -> big_shot_lib$panorama_texture.south;
            case 5 -> big_shot_lib$panorama_texture.north;
            default -> throw new IllegalStateException();
        });
    }
}
