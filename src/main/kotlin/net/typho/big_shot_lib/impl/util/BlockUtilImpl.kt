package net.typho.big_shot_lib.impl.util

//? if >=1.21.6 {
/*import net.minecraft.client.renderer.chunk.ChunkSectionLayer
*///? } else {
import net.minecraft.client.renderer.RenderType
//? }

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.client.rendering.util.BlockChunkLayer
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoVertexData
import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.math.vec.blockPos
import net.typho.big_shot_lib.api.util.BlockUtil
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.api.util.getExtensionValue
import net.typho.big_shot_lib.impl.client.rendering.util.VertexConsumerWrapper

object BlockUtilImpl : BlockUtil {
    override fun isSolidRender(
        state: BlockState,
        pos: IVec3<Int>,
        level: Level
    ): Boolean {
        //? if <1.21.2 {
        return state.isSolidRender(level, BlockPos(pos.x, pos.y, pos.z))
        //? } else {
        /*return state.isSolidRender
        *///? }
    }

    override fun getOffset(
        state: BlockState,
        pos: IVec3<Int>,
        level: Level
    ): IVec3<Float> {
        //? if <1.21.2 {
        return NeoVec3f(state.getOffset(level, BlockPos(pos.x, pos.y, pos.z)).toVector3f())
        //? } else {
        /*return NeoVec3f(state.getOffset(BlockPos(pos.x, pos.y, pos.z)).toVector3f())
        *///? }
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
            /*ChunkSectionLayer.SOLID -> BlockChunkLayer.SOLID
            ChunkSectionLayer.CUTOUT -> BlockChunkLayer.CUTOUT
            ChunkSectionLayer.CUTOUT_MIPPED -> BlockChunkLayer.CUTOUT
            ChunkSectionLayer.TRANSLUCENT -> BlockChunkLayer.TRANSLUCENT
            ChunkSectionLayer.TRIPWIRE -> BlockChunkLayer.TRIPWIRE
            *///? } else {
            RenderType.solid() -> BlockChunkLayer.SOLID
            RenderType.cutout(), RenderType.cutoutMipped() -> BlockChunkLayer.CUTOUT
            RenderType.translucent() -> BlockChunkLayer.TRANSLUCENT
            RenderType.tripwire() -> BlockChunkLayer.TRIPWIRE
            else -> null
            //? }
        }
    }

    override fun getFluidRenderSettings(state: FluidState): NeoRenderType {
        //? if >=1.21.11 {
        /*return when (ItemBlockRenderTypes.getRenderLayer(state)) {
            ChunkSectionLayer.SOLID -> NeoRenderSettings.BUILTINS.solid
            ChunkSectionLayer.CUTOUT -> NeoRenderSettings.BUILTINS.cutout
            ChunkSectionLayer.TRANSLUCENT -> NeoRenderSettings.BUILTINS.translucent
            ChunkSectionLayer.TRIPWIRE -> NeoRenderSettings.BUILTINS.tripwire
        }
        *///? } else if >=1.21.6 {
        /*return when (ItemBlockRenderTypes.getRenderLayer(state)) {
            ChunkSectionLayer.SOLID -> NeoRenderSettings.BUILTINS.solid
            ChunkSectionLayer.CUTOUT -> NeoRenderSettings.BUILTINS.cutout
            ChunkSectionLayer.CUTOUT_MIPPED -> NeoRenderSettings.BUILTINS.cutout
            ChunkSectionLayer.TRANSLUCENT -> NeoRenderSettings.BUILTINS.translucent
            ChunkSectionLayer.TRIPWIRE -> NeoRenderSettings.BUILTINS.tripwire
        }
        *///? } else {
        return ItemBlockRenderTypes.getRenderLayer(state).getExtensionValue()
        //? }
    }

    @Suppress("DEPRECATION")
    override fun shouldRenderFace(
        level: BlockGetter,
        pos: IVec3<Int>,
        direction: Direction,
        state: BlockState
    ): Boolean {
        val pos1 = pos + direction

        //? if >=1.21.2 {
        /*return Block.shouldRenderFace(state, level.getBlockState(BlockPos(pos1.x, pos1.y, pos1.z)), direction.mojang)
        *///? } else {
        return Block.shouldRenderFace(state, level, BlockPos(pos.x, pos.y, pos.z), direction.mojang, BlockPos(pos1.x, pos1.y, pos1.z))
        //? }
    }

    override fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: IVec3<Int>,
        out: (direction: Direction?, quads: List<NeoBakedQuad>) -> Unit
    ) {
        //? if <1.21.5 {
        val offset = BlockUtil.INSTANCE.getOffset(state, pos, level)
        val model = Minecraft.getInstance().blockRenderer.getBlockModel(state)
        val seed = state.getSeed(BlockPos(pos.x, pos.y, pos.z))

        fun face(face: Direction?, random: RandomSource) {
            random.setSeed(seed)
            //? fabric {
            /*val quads = model.getQuads(state, face?.mojang, random)
            *///? } neoforge {
            val modelData = model.getModelData(level, pos.blockPos, state, level.getModelData(pos.blockPos))
            val quads = model.getRenderTypes(state, random, modelData).flatMap { model.getQuads(state, face?.mojang, random, modelData, it) }
            //? }
            out(
                face,
                quads.mapTo(ArrayList(quads.size)) { WrapperUtil.INSTANCE.wrap(it) }
            )
        }

        fun faceWithOffset(face: Direction?, random: RandomSource) {
            random.setSeed(seed)
            //? fabric {
            /*val quads = model.getQuads(state, face?.mojang, random)
            *///? } neoforge {
            val modelData = model.getModelData(level, pos.blockPos, state, level.getModelData(pos.blockPos))
            val quads = model.getRenderTypes(state, random, modelData).flatMap { model.getQuads(state, face?.mojang, random, modelData, it) }
            //? }
            out(
                face,
                quads.mapTo(ArrayList(quads.size)) {
                    WrapperUtil.INSTANCE.wrap(it).withVertices { index, vertex ->
                        NeoVertexData(
                            vertex,
                            pos = vertex.pos + offset
                        )
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
        //? } else {
        /*val offset = BlockUtil.INSTANCE.getOffset(state, pos, level)
        val model = Minecraft.getInstance().blockRenderer.getBlockModel(state)
        val parts = model.collectParts(RandomSource.create(state.getSeed(BlockPos(pos.x, pos.y, pos.z))))

        fun face(face: Direction?) {
            val quads = parts.flatMap { it.getQuads(face?.mojang) }
            out(
                face,
                quads.mapTo(ArrayList(quads.size)) { WrapperUtil.INSTANCE.wrap(it) }
            )
        }

        fun faceWithOffset(face: Direction?) {
            val quads = parts.flatMap { it.getQuads(face?.mojang) }
            out(
                face,
                quads.mapTo(ArrayList(quads.size)) {
                    WrapperUtil.INSTANCE.wrap(it).withVertices { index, vertex ->
                        NeoVertexData(
                            vertex,
                            pos = vertex.pos + offset
                        )
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
        *///? }
    }

    override fun getFluidQuads(
        state: BlockState,
        fluid: FluidState,
        level: Level,
        pos: IVec3<Int>,
        occlusionCheck: (level: BlockGetter, from: BlockPos, direction: Direction, otherState: BlockState) -> Boolean,
        out: (quad: NeoBakedQuad) -> Unit
    ) {
        if (!fluid.isEmpty) {
            val consumer = FluidQuadConsumer(occlusionCheck, out)

            Minecraft.getInstance().blockRenderer.renderLiquid(
                pos.blockPos,
                level,
                VertexConsumerWrapper(consumer),
                state,
                fluid
            )

            consumer.flush()
        }
    }
}