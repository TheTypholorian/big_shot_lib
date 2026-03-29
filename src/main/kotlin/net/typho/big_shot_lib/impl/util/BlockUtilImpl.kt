package net.typho.big_shot_lib.impl.util

//? if >=1.21.6 {
import net.minecraft.client.renderer.chunk.ChunkSectionLayer
//? } else {
/*import net.minecraft.client.renderer.RenderType
*///? }

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.client.Minecraft
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.client.rendering.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.client.rendering.util.BlockChunkLayer
import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.AbstractVec3.Companion.plus
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.BlockUtil
import net.typho.big_shot_lib.api.util.WrapperUtil

object BlockUtilImpl : BlockUtil {
    override fun isSolidRender(
        state: BlockState,
        pos: AbstractVec3<Int>,
        level: Level
    ): Boolean {
        //? if <1.21.2 {
        /*return state.isSolidRender(level, BlockPos(pos.x, pos.y, pos.z))
        *///? } else {
        return state.isSolidRender
        //? }
    }

    override fun getOffset(
        state: BlockState,
        pos: AbstractVec3<Int>,
        level: Level
    ): AbstractVec3<Float> {
        //? if <1.21.2 {
        /*return NeoVec3f(state.getOffset(level, BlockPos(pos.x, pos.y, pos.z)).toVector3f())
        *///? } else {
        return NeoVec3f(state.getOffset(BlockPos(pos.x, pos.y, pos.z)).toVector3f())
        //? }
    }

    @Suppress("DEPRECATION")
    override fun getBlockChunkLayer(state: BlockState): BlockChunkLayer? {
        return when (ItemBlockRenderTypes.getChunkRenderType(state)) {
            //? if >=1.21.11 {
            /*ChunkSectionLayer.SOLID -> BlockChunkLayer.SOLID
            ChunkSectionLayer.CUTOUT -> BlockChunkLayer.CUTOUT
            ChunkSectionLayer.TRANSLUCENT -> BlockChunkLayer.TRANSLUCENT
            ChunkSectionLayer.TRIPWIRE -> BlockChunkLayer.TRIPWIRE
            *///? } else if >=1.21.6 {
            ChunkSectionLayer.SOLID -> BlockChunkLayer.SOLID
            ChunkSectionLayer.CUTOUT -> BlockChunkLayer.CUTOUT
            ChunkSectionLayer.CUTOUT_MIPPED -> BlockChunkLayer.CUTOUT
            ChunkSectionLayer.TRANSLUCENT -> BlockChunkLayer.TRANSLUCENT
            ChunkSectionLayer.TRIPWIRE -> BlockChunkLayer.TRIPWIRE
            //? } else {
            /*RenderType.solid() -> BlockChunkLayer.SOLID
            RenderType.cutout(), RenderType.cutoutMipped() -> BlockChunkLayer.CUTOUT
            RenderType.translucent() -> BlockChunkLayer.TRANSLUCENT
            RenderType.tripwire() -> BlockChunkLayer.TRIPWIRE
            else -> null
            *///? }
        }
    }

    @Suppress("DEPRECATION")
    override fun shouldRenderFace(
        level: BlockGetter,
        pos: AbstractVec3<Int>,
        direction: NeoDirection,
        state: BlockState
    ): Boolean {
        val pos1 = pos + direction

        //? if >=1.21.2 {
        return Block.shouldRenderFace(state, level.getBlockState(BlockPos(pos1.x, pos1.y, pos1.z)), direction.mojang)
        //? } else {
        /*return Block.shouldRenderFace(state, level, BlockPos(pos1.x, pos1.y, pos1.z), direction.mojang, BlockPos(pos.x, pos.y, pos.z))
        *///? }
    }

    override fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: AbstractVec3<Int>,
        out: (direction: NeoDirection?, quads: List<NeoBakedQuad>) -> Unit
    ) {
        //? if <1.21.5 {
        /*val offset = BlockUtil.INSTANCE.getOffset(state, pos, level)
        val model = Minecraft.getInstance().blockRenderer.getBlockModel(state)
        val seed = state.getSeed(BlockPos(pos.x, pos.y, pos.z))

        fun face(face: NeoDirection?, random: RandomSource) {
            random.setSeed(seed)
            val quads = model.getQuads(state, face?.mojang, random)
            out(
                face,
                quads.mapTo(ArrayList(quads.size)) { WrapperUtil.INSTANCE.wrap(it) }
            )
        }

        fun faceWithOffset(face: NeoDirection?, random: RandomSource) {
            random.setSeed(seed)
            val quads = model.getQuads(state, face?.mojang, random)
            out(
                face,
                quads.mapTo(ArrayList(quads.size)) {
                    WrapperUtil.INSTANCE.wrap(it).withVertices { index, vertex ->
                        vertex.withPosition { pos -> pos + offset }
                    }
                }
            )
        }

        val random = RandomSource.create(seed)

        if (offset.equals(0f, 0f, 0f)) {
            face(NeoDirection.DOWN, random)
            face(NeoDirection.UP, random.also { it.setSeed(seed) })
            face(NeoDirection.NORTH, random.also { it.setSeed(seed) })
            face(NeoDirection.SOUTH, random.also { it.setSeed(seed) })
            face(NeoDirection.WEST, random.also { it.setSeed(seed) })
            face(NeoDirection.EAST, random.also { it.setSeed(seed) })
            face(null, random.also { it.setSeed(seed) })
        } else {
            faceWithOffset(NeoDirection.DOWN, random)
            faceWithOffset(NeoDirection.UP, random.also { it.setSeed(seed) })
            faceWithOffset(NeoDirection.NORTH, random.also { it.setSeed(seed) })
            faceWithOffset(NeoDirection.SOUTH, random.also { it.setSeed(seed) })
            faceWithOffset(NeoDirection.WEST, random.also { it.setSeed(seed) })
            faceWithOffset(NeoDirection.EAST, random.also { it.setSeed(seed) })
            faceWithOffset(null, random.also { it.setSeed(seed) })
        }
        *///? } else {
        val offset = BlockUtil.INSTANCE.getOffset(state, pos, level)
        val model = Minecraft.getInstance().blockRenderer.getBlockModel(state)
        val parts = model.collectParts(RandomSource.create(state.getSeed(BlockPos(pos.x, pos.y, pos.z))))

        fun face(face: NeoDirection?) {
            val quads = parts.flatMap { it.getQuads(face?.mojang) }
            out(
                face,
                quads.mapTo(ArrayList(quads.size)) { WrapperUtil.INSTANCE.wrap(it) }
            )
        }

        fun faceWithOffset(face: NeoDirection?) {
            val quads = parts.flatMap { it.getQuads(face?.mojang) }
            out(
                face,
                quads.mapTo(ArrayList(quads.size)) {
                    WrapperUtil.INSTANCE.wrap(it).withVertices { index, vertex ->
                        vertex.withPosition { pos -> pos + offset }
                    }
                }
            )
        }

        if (offset.equals(0f, 0f, 0f)) {
            face(NeoDirection.DOWN)
            face(NeoDirection.UP)
            face(NeoDirection.NORTH)
            face(NeoDirection.SOUTH)
            face(NeoDirection.WEST)
            face(NeoDirection.EAST)
            face(null)
        } else {
            faceWithOffset(NeoDirection.DOWN)
            faceWithOffset(NeoDirection.UP)
            faceWithOffset(NeoDirection.NORTH)
            faceWithOffset(NeoDirection.SOUTH)
            faceWithOffset(NeoDirection.WEST)
            faceWithOffset(NeoDirection.EAST)
            faceWithOffset(null)
        }
        //? }
    }
}