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
                mesh.upload(4) {
                    vertex(1f, 1f, 0f).textureUV(1f, 1f)
                    vertex(-1f, 1f, 0f).textureUV(0f, 1f)
                    vertex(-1f, -1f, 0f).textureUV(0f, 0f)
                    vertex(1f, -1f, 0f).textureUV(1f, 0f)
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

    fun upload(vertexCount: Int, out: Builder.() -> Unit) {
        builder(vertexCount)?.use { out(it) }
    }

    fun lazyUpload(vertexCount: Int, out: Builder.() -> Unit): () -> Unit {
        if (!writeMode.canLazyUpload) {
            throw UnsupportedOperationException("Buffer write mode $writeMode does not support lazy uploading")
        }

        val builder = builder(vertexCount) ?: return {}
        out(builder)
        return builder::free
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

    inner class Builder(
        @JvmField
        val bufferBuilder: NeoBufferBuilder
    ) : NeoVertexConsumer.Redirect(bufferBuilder), NativeResource {
        override fun free() {
            val built = bufferBuilder.build() ?: throw IllegalStateException("Error with neo buffer builder")

            if (ebo == null) {
                size = built.vertexCount
                indexType = null
            } else {
                size = built.indexCount!!
                indexType = built.indexType
            }

            vao.bind().use {
                vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
                    format.initVertexArrayState()
                }
            }
        }
    }
}