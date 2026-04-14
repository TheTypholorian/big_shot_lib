package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlIndexDataType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBufferWriter
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlVertexArray
import net.typho.big_shot_lib.api.util.buffer.BYTE_MASK
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import net.typho.big_shot_lib.api.util.buffer.SHORT_MASK
import org.lwjgl.system.NativeResource

open class Mesh(
    @JvmField
    val format: NeoVertexFormat,
    @JvmField
    val mode: GlBeginMode,
    @JvmField
    val writeMode: GlBufferWriter.Mode,
    @JvmField
    val usage: GlBufferUsage,
    @JvmField
    val vao: GlVertexArray = NeoGlVertexArray(),
    @JvmField
    val vbo: GlBuffer = NeoGlBuffer(),
    @JvmField
    val ebo: GlBuffer? = if (mode.indexData == null) null else NeoGlBuffer()
) : Renderable, NativeResource {
    companion object {
        @JvmStatic
        val SCREEN_MESH by lazy {
            Mesh(
                NeoVertexFormat.POSITION_TEX,
                GlBeginMode.QUADS,
                GlBufferWriter.Mode.REGULAR,
                GlBufferUsage.STATIC_DRAW
            ).also { mesh ->
                mesh.builder(4)?.use {
                    it.vertex(1f, 1f, 0f).textureUV(1f, 1f)
                    it.vertex(-1f, 1f, 0f).textureUV(0f, 1f)
                    it.vertex(-1f, -1f, 0f).textureUV(0f, 0f)
                    it.vertex(1f, -1f, 0f).textureUV(1f, 0f)
                }
            }
        }
    }

    var size: Int = 0
        protected set
    var indexType: GlIndexDataType? = null
        protected set

    override fun free() {
        vao.free()
        vbo.free()
        ebo?.free()
    }

    fun builder(vertexCount: Int): Builder? {
        if (vertexCount == 0) {
            size = 0
            return null
        }

        return if (ebo == null) {
            Builder(
                NeoBufferBuilder(
                    format,
                    mode,
                    vertexCount,
                    { writeMode.create(vbo, GlBufferTarget.ARRAY_BUFFER, it, usage) },
                    { null }
                )
            )
        } else {
            Builder(
                NeoBufferBuilder(
                    format,
                    mode,
                    vertexCount,
                    { writeMode.create(vbo, GlBufferTarget.ARRAY_BUFFER, it, usage) },
                    { writeMode.create(ebo, GlBufferTarget.ELEMENT_ARRAY_BUFFER, it!!, usage) }
                )
            )
        }
    }

    fun rawUpload(size: Int, vertices: NeoBuffer) {
        this.size = size
        indexType = null

        vao.bind().use {
            vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
                it.bufferData(vertices, usage)
                format.initVertexArrayState()
            }
        }
    }

    fun rawUpload(size: Int, indexType: GlIndexDataType, vertices: NeoBuffer, indices: NeoBuffer) {
        this.size = size
        this.indexType = indexType

        vao.bind().use {
            ebo!!.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER).use {
                it.bufferData(indices, usage)
            }

            vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
                it.bufferData(vertices, usage)
                format.initVertexArrayState()
            }
        }
    }

    fun rawUpload(size: Int, indexType: GlIndexDataType, indices: NeoBuffer) {
        this.size = size
        this.indexType = indexType

        vao.bind().use {
            ebo!!.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER).use {
                it.bufferData(indices, usage)
            }
        }
    }

    fun rawUpload(size: Int, indexType: GlIndexDataType) {
        this.size = size
        this.indexType = indexType
    }

    fun generateIndices(numVertices: Int): NeoBuffer.GCNative {
        val indexData = mode.indexData ?: throw IllegalStateException("Mode $mode is not indexed")
        val primitiveCount = numVertices / mode.indexData.stride
        val indexCount = primitiveCount * mode.indexData.offsets.size
        val indexType = when (indexCount) {
            indexCount and BYTE_MASK -> GlIndexDataType.BYTE
            indexCount and SHORT_MASK -> GlIndexDataType.SHORT
            else -> GlIndexDataType.INT
        }
        val indexBuffer = NeoBuffer.GCNative(indexCount.toLong() * format.vertexSizeBytes)

        indexBuffer.write().run {
            var vertex = 0

            repeat(numVertices / indexData.stride) {
                for (n in indexData.offsets) {
                    indexType.write(this, n + vertex)
                }

                vertex += indexData.stride
            }
        }

        return indexBuffer
    }

    fun initVertexArrayState() {
        vao.bind().use {
            vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
                format.initVertexArrayState()
            }
        }
    }

    override fun draw() {
        if (size > 0) {
            vao.bind().use { vao ->
                if (ebo == null) {
                    vao.drawArrays(mode, 0, size)
                } else {
                    ebo.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER).use {
                        vao.drawElements(mode, size, indexType!!, 0L)
                    }
                }
            }
        }
    }

    fun drawInstanced(instanceCount: Int) {
        if (size > 0) {
            vao.bind().use { vao ->
                if (ebo == null) {
                    vao.drawArraysInstanced(mode, 0, size, instanceCount)
                } else {
                    ebo.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER).use {
                        vao.drawElementsInstanced(mode, size, indexType!!, 0L, instanceCount)
                    }
                }
            }
        }
    }

    inner class Builder(
        @JvmField
        val bufferBuilder: NeoBufferBuilder
    ) : NeoVertexConsumer.Redirect(bufferBuilder), NativeResource {
        fun build() {
            val built = bufferBuilder.build() ?: throw IllegalStateException("Error with neo buffer builder")

            if (ebo == null) {
                size = built.vertexCount
                indexType = null
            } else {
                size = built.indexCount!!
                indexType = built.indexType
            }

            initVertexArrayState()
        }

        override fun free() = build()
    }
}