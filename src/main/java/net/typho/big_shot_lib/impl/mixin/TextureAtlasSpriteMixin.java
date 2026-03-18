package net.typho.big_shot_lib.impl.mixin;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoAtlas;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoAtlasSprite;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TextureAtlasSprite.class)
public class TextureAtlasSpriteMixin implements ImmutableExtension<NeoAtlasSprite> {
    @Shadow
    @Final
    int x;
    @Shadow
    @Final
    int y;
    @Shadow
    @Final
    private float u0;
    @Shadow
    @Final
    private float u1;
    @Shadow
    @Final
    private float v0;
    @Shadow
    @Final
    private float v1;
    @Shadow
    @Final
    private SpriteContents contents;
    @Shadow
    @Final
    private ResourceLocation atlasLocation;
    @Unique
    private final NeoAtlasSprite big_shot_lib$extension_value = new NeoAtlasSprite() {
        @Override
        public @NotNull NeoAtlas getAtlas() {
            throw new UnsupportedOperationException("TODO atlases"); // TODO
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public int getWidth() {
            return contents.width();
        }

        @Override
        public int getHeight() {
            return contents.height();
        }

        @Override
        public float getU0() {
            return u0;
        }

        @Override
        public float getU1() {
            return u1;
        }

        @Override
        public float getV0() {
            return v0;
        }

        @Override
        public float getV1() {
            return v1;
        }
    };

    @Override
    public NeoAtlasSprite getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
