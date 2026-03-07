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
        val parts = model.collectParts(RandomSource.create(state.getSeed(pos)))

        fun face(face: Direction?) {
            val quads = parts.flatMap { it.getQuads(face) }
            out.accept(
                face,
                quads.mapTo(ArrayList(quads.size)) { WrapperUtil.INSTANCE.wrap(it) }
            )
        }

        fun faceWithOffset(face: Direction?) {
            val quads = parts.flatMap { it.getQuads(face) }
            out.accept(
                face,
                quads.mapTo(ArrayList(quads.size)) {
                    WrapperUtil.INSTANCE.wrap(it).withVertices { index, vertex ->
                        vertex.withPosition { pos -> pos.add(offset, Vector3f()) }
                    }
                }
            )
        }

        if (offset.equals(0f, 0f, 0f)) {
            face(Direction.DOWN)
            face(Direction.UP)
            face(Direction.NORTH)
            face(Direction.SOUTH)
            face(Direction.WEST)
            face(Direction.EAST)
            face(null)
        } else {
            faceWithOffset(Direction.DOWN)
            faceWithOffset(Direction.UP)
            faceWithOffset(Direction.NORTH)
            faceWithOffset(Direction.SOUTH)
            faceWithOffset(Direction.WEST)
            faceWithOffset(Direction.EAST)
            faceWithOffset(null)
        }
    }
}