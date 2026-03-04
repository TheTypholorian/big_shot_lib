package net.typho.big_shot_lib.mixin.util;

import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CubeMap.class)
public interface CubeMapAccessor {
    @Accessor("images")
    ResourceLocation[] getImages();
}
