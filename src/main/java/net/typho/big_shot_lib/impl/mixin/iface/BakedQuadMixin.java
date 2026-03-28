package net.typho.big_shot_lib.impl.mixin.iface;

//? if >=1.21.11 {
import org.joml.Vector3fc;
import net.typho.big_shot_lib.api.math.vec.NeoVec2f;
import net.typho.big_shot_lib.api.math.vec.NeoVec3f;
//? }

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoAtlasSprite;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoBakedQuad;
import net.typho.big_shot_lib.api.client.rendering.quad.NeoVertexData;
import net.typho.big_shot_lib.api.math.NeoDirection;
import net.typho.big_shot_lib.api.math.NeoDirectionKt;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin implements ImmutableExtension<NeoBakedQuad> {
    //? if <1.21.11 {
    /*@Shadow
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
    @Final
    private boolean shade;
    @Unique
    private final NeoBakedQuad big_shot_lib$extension_value = new NeoBakedQuad() {
        private NeoVertexData[] vertices;

        @Override
        @NotNull
        @SuppressWarnings("NullableProblems")
        public NeoVertexData[] getVertices() {
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
            return direction == null ? null : NeoDirectionKt.getNeo(direction);
        }

        @Override
        @NotNull
        public NeoAtlasSprite getSprite() {
            return ImmutableExtensionKt.getExtensionValue(sprite);
        }

        @Override
        public boolean getShade() {
            return shade;
        }
    };
    *///? } else {
    @Shadow
    @Final
    private int tintIndex;

    @Shadow
    public abstract boolean isTinted();

    @Shadow
    @Final
    private Direction direction;
    @Shadow
    @Final
    private TextureAtlasSprite sprite;
    @Shadow
    @Final
    private boolean shade;
    @Shadow
    @Final
    private Vector3fc position0;
    @Shadow
    @Final
    private Vector3fc position1;
    @Shadow
    @Final
    private Vector3fc position2;
    @Shadow
    @Final
    private Vector3fc position3;
    @Shadow
    @Final
    private long packedUV0;
    @Shadow
    @Final
    private long packedUV1;
    @Shadow
    @Final
    private long packedUV2;
    @Shadow
    @Final
    private long packedUV3;

    @Unique
    private final NeoBakedQuad big_shot_lib$extension_value = new NeoBakedQuad() {
        @Override
        @NotNull
        @SuppressWarnings("NullableProblems")
        public NeoVertexData[] getVertices() {
            return new NeoVertexData[]{
                    new NeoVertexData.PositionTexture(new NeoVec3f(position0), new NeoVec2f(packedUV0)),
                    new NeoVertexData.PositionTexture(new NeoVec3f(position1), new NeoVec2f(packedUV1)),
                    new NeoVertexData.PositionTexture(new NeoVec3f(position2), new NeoVec2f(packedUV2)),
                    new NeoVertexData.PositionTexture(new NeoVec3f(position3), new NeoVec2f(packedUV3))
            };
        }

        @Override
        @Nullable
        public Integer getTintIndex() {
            return isTinted() ? tintIndex : null;
        }

        @Override
        @Nullable
        public NeoDirection getDirection() {
            return direction == null ? null : NeoDirectionKt.getNeo(direction);
        }

        @Override
        @NotNull
        public NeoAtlasSprite getSprite() {
            return ImmutableExtensionKt.getExtensionValue(sprite);
        }

        @Override
        public boolean getShade() {
            return shade;
        }
    };
    //? }

    @Override
    public NeoBakedQuad getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
