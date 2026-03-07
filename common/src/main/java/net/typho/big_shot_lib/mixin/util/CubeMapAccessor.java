package net.typho.big_shot_lib.mixin.util;

import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(CubeMap.class)
public interface CubeMapAccessor {
    @Accessor("sides")
    List<ResourceLocation> getSides();
}
