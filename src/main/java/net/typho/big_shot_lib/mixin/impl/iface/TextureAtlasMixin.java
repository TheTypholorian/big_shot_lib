package net.typho.big_shot_lib.mixin.impl.iface;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import kotlin.collections.MapsKt;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture2D;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager;
import net.typho.big_shot_lib.api.client.rendering.util.NeoAtlas;
import net.typho.big_shot_lib.api.client.rendering.util.NeoAtlasSprite;
import net.typho.big_shot_lib.api.util.resource.Identifier;
import net.typho.big_shot_lib.impl.IdentifierUtilKt;
import net.typho.big_shot_lib.impl.client.rendering.internal.BoundMinecraftTexture;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin extends AbstractTexture implements ImmutableExtension<NeoAtlas> {
    @Shadow
    private int width;
    @Shadow
    private int height;

    //? if <1.21.11 {
    @Shadow
    private Map<Identifier, TextureAtlasSprite> texturesByName;

    @Shadow
    public abstract Identifier location();
    //? } else {
    
    /*@Shadow
    private Map<Identifier, TextureAtlasSprite> texturesByName;

    @Shadow
    public abstract Identifier location();
    *///? }

    @Override
    public NeoAtlas getBig_shot_lib$extension_value() {
        return new NeoAtlas() {
            @Override
            public @NotNull Identifier getLocation() {
                return IdentifierUtilKt.getNeo(location());
            }

            @Override
            public int getGlId() {
                //? if <1.21.5 {
                return id;
                //? } else {
                /*GlTexture2D texture = ImmutableExtensionKt.getExtensionValue(TextureAtlasMixin.this.texture);
                return texture == null ? -1 : texture.getGlId();
                 *///? }
            }

            @Override
            public boolean getFreed() {
                return getGlId() != -1;
            }

            @Override
            public void free() {
                TextureAtlasMixin.this.close();
            }

            @Override
            public GlResourceType getType() {
                return GlResourceType.TEXTURE;
            }

            @Override
            public GlTextureFormat getFormat() {
                return GlTextureFormat.RGBA8;
            }

            @Override
            public @NotNull GlBoundTexture2D bind(@NotNull GlTextureTarget target) {
                return new BoundMinecraftTexture(
                        this,
                        target,
                        NeoGlStateManager.getMAIN().getTextures().get(target).push(getGlId()),
                        width,
                        height,
                        getFormat()
                );
            }

            @Override
            public @NotNull Map<Identifier, NeoAtlasSprite> getSprites() {
                return MapsKt.mapValues(
                        MapsKt.mapKeys(
                                texturesByName,
                                id -> IdentifierUtilKt.getNeo(id.getKey())
                        ),
                        sprite -> ImmutableExtensionKt.getExtensionValue(sprite.getValue(), NeoAtlasSprite.class)
                );
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }
        };
    }
}