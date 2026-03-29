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
                NeoVertexFormat.BLIT_SCREEN,
                GlBeginMode.QUADS,
                GlBufferUsage.STATIC_DRAW
            ).also { mesh ->
                mesh.upload(4) {
                    vertex(1f, 1f, 0f)
                    vertex(-1f, 1f, 0f)
                    vertex(-1f, -1f, 0f)
                    vertex(1f, -1f, 0f)
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

    fun upload(built: NeoBufferBuilder.Built) {
        if (ebo == null) {
            size = built.vertexCount
            indexType = null
        } else {
            size = built.indexCount!!
            indexType = built.indexType
        }
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
                            vbo.bufferData(it!!, usage)
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

    inner class Builder(
        @JvmField
        val bufferBuilder: NeoBufferBuilder,
        @JvmField
        val upload: Boolean
    ) : NeoVertexConsumer.Redirect(bufferBuilder), NativeResource {
        override fun free() {
            val built = bufferBuilder.build() ?: throw IllegalStateException("Error with neo buffer builder")
            upload(built)

            if (upload) {
                vbo.bind(GlBufferTarget.ARRAY_BUFFER).use { it.bufferData(built.vertexBuffer, usage) }
                ebo?.bind(GlBufferTarget.ELEMENT_ARRAY_BUFFER)?.use { it.bufferData(built.indexBuffer!!, usage) }
            }

            vao.bind().use {
                vbo.bind(GlBufferTarget.ARRAY_BUFFER).use {
                    TODO("vao state")
                }
            }

            bufferBuilder.free()
        }
    }
}