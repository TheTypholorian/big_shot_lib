package net.typho.big_shot_lib.mixin.impl.iface;

//? if >=1.21.11 {
/*import org.joml.Vector3fc;
import net.typho.big_shot_lib.api.math.vec.NeoVec2f;
import net.typho.big_shot_lib.api.math.vec.NeoVec3f;
*///? }

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoBakedQuad;
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoVertexData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin implements NeoBakedQuad {
    @Unique
    private NeoVertexData[] vibrancy$vertices;

    //? if <1.21.11 {
    @Shadow
    @Final
    protected int[] vertices;

    @Override
    @NotNull
    public NeoVertexData[] getVertices() {
        if (vibrancy$vertices == null) {
            vibrancy$vertices = new NeoVertexData[]{
                    new NeoVertexData(vertices, 0),
                    new NeoVertexData(vertices, 8),
                    new NeoVertexData(vertices, 16),
                    new NeoVertexData(vertices, 24)
            };
        }

        return vibrancy$vertices;
    }
    //? } else {
    /*@Shadow
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

    @Override
    @NotNull
    @SuppressWarnings("NullableProblems")
    public NeoVertexData[] getVertices() {
        return
        if (vibrancy$vertices == null) {
            vibrancy$vertices = new NeoVertexData[]{
                    new NeoVertexData(new NeoVec3f(position0), null, new NeoVec2f(packedUV0), null, null, null),
                    new NeoVertexData(new NeoVec3f(position1), null, new NeoVec2f(packedUV1), null, null, null),
                    new NeoVertexData(new NeoVec3f(position2), null, new NeoVec2f(packedUV2), null, null, null),
                    new NeoVertexData(new NeoVec3f(position3), null, new NeoVec2f(packedUV3), null, null, null)
            };
        }

        return vibrancy$vertices;
    }
    *///? }
}
