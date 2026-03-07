package net.typho.big_shot_lib.mixin.util;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(TextureAtlas.class)
public interface TextureAtlasAccessor {
    @Accessor("width")
    int getWidth();

    @Accessor("height")
    int getHeight();

    @Accessor("mipLevelCount")
    int getMipLevelCount();

    @Accessor("texturesByName")
    Map<Identifier, TextureAtlasSprite> getTexturesByName();
}
