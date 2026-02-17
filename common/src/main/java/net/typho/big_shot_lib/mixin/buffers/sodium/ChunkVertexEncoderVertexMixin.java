package net.typho.big_shot_lib.mixin.buffers.sodium;

import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.typho.big_shot_lib.impl.buffers.sodium.VertexWithNormal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ChunkVertexEncoder.Vertex.class, remap = false)
public class ChunkVertexEncoderVertexMixin implements VertexWithNormal {
    @Unique
    private int big_shot_lib$normal;

    @Override
    public int big_shot_lib$getNormal() {
        return big_shot_lib$normal;
    }

    @Override
    public void big_shot_lib$setNormal(int n) {
        big_shot_lib$normal = n;
    }
}
