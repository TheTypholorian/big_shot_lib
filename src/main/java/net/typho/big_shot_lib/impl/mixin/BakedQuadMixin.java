package net.typho.big_shot_lib.impl.mixin;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoAtlasSprite;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoBakedQuad;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoVertexData;
import net.typho.big_shot_lib.api.math.NeoDirection;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin implements ImmutableExtension<NeoBakedQuad> {
    @Shadow
    @Final
    protected int[] vertices;
    @Shadow
    @Final
    protected int tintIndex;

    @Shadow
    public abstract boolean isTinted();

    @Shadow
    @Final
    protected Direction direction;
    @Shadow
    @Final
    protected TextureAtlasSprite sprite;

    @Shadow
    public abstract boolean isShade();

    @Unique
    private final NeoBakedQuad big_shot_lib$extension_value = new NeoBakedQuad() {
        private NeoVertexData[] vertices;

        @Override
        @NotNull
        public NeoVertexData @NotNull [] getVertices() {
            if (vertices == null) {
                vertices = new NeoVertexData[]{
                        new NeoVertexData.LazyPacked(BakedQuadMixin.this.vertices, 0),
                        new NeoVertexData.LazyPacked(BakedQuadMixin.this.vertices, 8),
                        new NeoVertexData.LazyPacked(BakedQuadMixin.this.vertices, 16),
                        new NeoVertexData.LazyPacked(BakedQuadMixin.this.vertices, 24)
                };
            }

            return vertices;
        }

        @Override
        @Nullable
        public Integer getTintIndex() {
            return isTinted() ? tintIndex : null;
        }

        @Override
        @Nullable
        public NeoDirection getDirection() {
            if (direction == null) {
                return null;
            }

            return switch (direction) {
                case DOWN -> NeoDirection.DOWN;
                case UP -> NeoDirection.UP;
                case NORTH -> NeoDirection.NORTH;
                case SOUTH -> NeoDirection.SOUTH;
                case WEST -> NeoDirection.WEST;
                case EAST -> NeoDirection.EAST;
            };
        }

        @Override
        @NotNull
        @SuppressWarnings("UNCHECKED_CAST")
        public NeoAtlasSprite getSprite() {
            return ImmutableExtensionKt.getExtensionValue(sprite);
        }

        @Override
        public boolean getShade() {
            return isShade();
        }
    };

    @Override
    public NeoBakedQuad getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
