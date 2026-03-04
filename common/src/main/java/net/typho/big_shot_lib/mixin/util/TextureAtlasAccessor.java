package net.typho.big_shot_lib.mixin.util;

import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextureAtlas.class)
public interface TextureAtlasAccessor {
    @Accessor("width")
    int getWidth();

    @Accessor("height")
    int getHeight();
}
