package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlIndexDataType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBufferWriter
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlVertexArray
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.system.NativeResource
import kotlin.io.use

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

    var vao: GlVertexArray? = null
        protected set
    var size: Int = 0
        protected set
    var indexType: GlIndexDataType? = null
        protected set

    override fun free() {
        GlQueue.INSTANCE.runOrQueue {
            vao?.free()
            vbo.free()
            ebo?.free()
        }
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

        vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
            it.bufferData(vertices, usage)
        }
    }

    fun rawUpload(size: Int, indexType: GlIndexDataType, vertices: NeoBuffer, indices: NeoBuffer) {
        this.size = size
        this.indexType = indexType

        ebo!!.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER).use {
            it.bufferData(indices, usage)
        }

        vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
            it.bufferData(vertices, usage)
        }
    }

    protected fun _draw(vao: GlBoundVertexArray) {
        if (ebo == null) {
            vao.drawArrays(mode, 0, size)
        } else {
            ebo.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER).use {
                vao.drawElements(mode, size, indexType!!, 0L)
            }
        }
    }

    override fun draw() {
        if (size > 0) {
            vao?.let { vao ->
                vao.bind().use { vao -> _draw(vao) }
            } ?: {
                val vao = NeoGlVertexArray()
                this.vao = vao

                vao.bind().use { vao ->
                    vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
                        format.initVertexArrayState()
                    }

                    _draw(vao)
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
        }

        override fun free() = build()
    }
}