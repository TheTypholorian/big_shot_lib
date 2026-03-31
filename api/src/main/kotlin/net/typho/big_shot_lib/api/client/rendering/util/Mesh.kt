package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferAccess
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlIndexDataType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlMappedBuffer
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

    /**
     * Only use this method when you have no way of knowing the buffer size (ex. when invoking entity rendering). Otherwise, always use upload method and specify vertex count, as it's much faster.
     */
    fun builder() = Builder(NeoBufferBuilder.create(format, mode), true)

    /**
     * Only use this method when you have no way of knowing the buffer size (ex. when invoking entity rendering). Otherwise, always use the other method (that takes a vertex count), as it's much faster.
     */
    fun upload(out: Builder.() -> Unit) {
        builder().also { out(it) }.free()
    }

    fun upload(vertexCount: Int, out: Builder.() -> Unit) {
        if (vertexCount == 0) {
            size = 0
            return
        }

        vbo.bind(GlBufferTarget.ARRAY_BUFFER).use { vbo ->
            ebo?.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER)?.use { ebo ->
                Builder(
                    NeoBufferBuilder.create(
                        format,
                        mode,
                        vertexCount,
                        {
                            vbo.bufferData(it, usage)
                            vbo.mapBuffer(GlBufferAccess.WRITE_ONLY, it)
                        },
                        {
                            ebo.bufferData(it!!, usage)
                            ebo.mapBuffer(GlBufferAccess.WRITE_ONLY, it)
                        }
                    ),
                    false
                ).also { out(it) }.free()
            }
                ?: Builder(
                    NeoBufferBuilder.create(
                        format,
                        mode,
                        vertexCount,
                        {
                            vbo.bufferData(it, usage)
                            vbo.mapBuffer(GlBufferAccess.WRITE_ONLY, it)
                        },
                        { null }
                    ),
                    false
                ).also { out(it) }.free()
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

    inner class Builder(
        @JvmField
        val bufferBuilder: NeoBufferBuilder,
        @JvmField
        val upload: Boolean
    ) : NeoVertexConsumer.Redirect(bufferBuilder), NativeResource {
        override fun free() {
            (bufferBuilder.build() ?: throw IllegalStateException("Error with neo buffer builder")).use { built ->
                if (ebo == null) {
                    size = built.vertexCount
                    indexType = null
                } else {
                    size = built.indexCount!!
                    indexType = built.indexType
                }

                if (upload) {
                    vbo.bind(GlBufferTarget.ARRAY_BUFFER).use { it.bufferData(built.vertexBuffer, usage) }
                    ebo?.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER)?.use { it.bufferData(built.indexBuffer!!, usage) }
                }
            }

            vao.bind().use {
                vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
                    format.initVertexArrayState()
                }
            }
        }
    }
}