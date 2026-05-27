package net.typho.big_shot_lib.mixin.impl.panorama;

import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.typho.big_shot_lib.api.client.rendering.PanoramaRendererExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PanoramaRenderer.class)
public class PanoramaRendererMixin implements PanoramaRendererExtension {
    @Shadow
    @Final
    @Mutable
    private CubeMap cubeMap;

    @Override
    public @NotNull CubeMap getCubeMap() {
        return cubeMap;
    }

    @Override
    public void setCubeMap(@NotNull CubeMap cubeMap) {
        this.cubeMap = cubeMap;
    }
}
