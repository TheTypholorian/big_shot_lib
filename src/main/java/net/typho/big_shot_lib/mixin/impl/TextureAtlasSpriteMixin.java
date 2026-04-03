package net.typho.big_shot_lib.mixin.impl;

//? if <1.21.11 {
/*import net.minecraft.resources.ResourceLocation;
*///? } else {
import net.minecraft.resources.Identifier;
//? }

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoAtlas;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoAtlasSprite;
import net.typho.big_shot_lib.impl.IdentifierUtilKt;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(TextureAtlasSprite.class)
public class TextureAtlasSpriteMixin implements ImmutableExtension<NeoAtlasSprite> {
    @Shadow
    @Final
    private int x;
    @Shadow
    @Final
    private int y;
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
    //? if <1.21.11 {
    /*private ResourceLocation atlasLocation;
    *///? } else {
    private Identifier atlasLocation;
    //? }
    @Unique
    private final NeoAtlasSprite big_shot_lib$extension_value = new NeoAtlasSprite() {
        @Override
        public @NotNull NeoAtlas getAtlas() {
            return NeoAtlas.get(IdentifierUtilKt.getNeo(atlasLocation));
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
