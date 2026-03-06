package net.typho.big_shot_lib.impl.util

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.client.opengl.util.MeshUtil
import net.typho.big_shot_lib.api.client.util.quads.NeoBakedQuad
import net.typho.big_shot_lib.api.util.BlockUtil
import net.typho.big_shot_lib.api.util.WrapperUtil
import org.joml.Vector3f
import java.util.function.BiConsumer

class MeshUtilImpl : MeshUtil {
    @Suppress("DEPRECATION")
    override fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        out: BiConsumer<Direction?, List<NeoBakedQuad>>
    ) {
        val offset = BlockUtil.INSTANCE.getOffset(state, pos, level)
        val model = Minecraft.getInstance().blockRenderer.getBlockModel(state)
        val seed = state.getSeed(pos)

        fun face(face: Direction?, random: RandomSource) {
            random.setSeed(seed)
            val quads = model.getQuads(state, face, random)
            out.accept(
                face,
                quads.mapTo(ArrayList(quads.size)) { WrapperUtil.INSTANCE.wrap(it) }
            )
        }

        fun faceWithOffset(face: Direction?, random: RandomSource) {
            random.setSeed(seed)
            val quads = model.getQuads(state, face, random)
            out.accept(
                face,
                quads.mapTo(ArrayList(quads.size)) {
                    WrapperUtil.INSTANCE.wrap(it).withVertices { index, vertex ->
                        vertex.withPosition { pos -> pos.add(offset, Vector3f()) }
                    }
                }
            )
        }

        val random = RandomSource.create(seed)

        if (offset.equals(0f, 0f, 0f)) {
            face(Direction.DOWN, random)
            face(Direction.UP, random.also { it.setSeed(seed) })
            face(Direction.NORTH, random.also { it.setSeed(seed) })
            face(Direction.SOUTH, random.also { it.setSeed(seed) })
            face(Direction.WEST, random.also { it.setSeed(seed) })
            face(Direction.EAST, random.also { it.setSeed(seed) })
            face(null, random.also { it.setSeed(seed) })
        } else {
            faceWithOffset(Direction.DOWN, random)
            faceWithOffset(Direction.UP, random.also { it.setSeed(seed) })
            faceWithOffset(Direction.NORTH, random.also { it.setSeed(seed) })
            faceWithOffset(Direction.SOUTH, random.also { it.setSeed(seed) })
            faceWithOffset(Direction.WEST, random.also { it.setSeed(seed) })
            faceWithOffset(Direction.EAST, random.also { it.setSeed(seed) })
            faceWithOffset(null, random.also { it.setSeed(seed) })
        }
    }
}