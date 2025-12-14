package net.typho.big_shot_lib.api

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.GlResourceType
import net.typho.big_shot_lib.gl.TextureFormat
import net.typho.big_shot_lib.gl.Unbindable
import org.joml.Vector4f
import org.lwjgl.opengl.GL30.*
import java.util.*

open class NeoFramebuffer(
    protected val location: ResourceLocation,
    protected val id: Int,
    val colorAttachments: Array<IFramebufferAttachment>,
    val depthAttachment: IFramebufferAttachment?,
    protected var width: Int,
    protected var height: Int
) : IFramebuffer {
    companion object {
        val AUTO_RESIZE = LinkedList<NeoFramebuffer>()
        val AUTO_CLEAR = LinkedHashMap<NeoFramebuffer, Vector4f>()
    }

    constructor(
        location: ResourceLocation,
        colorAttachments: Array<IFramebufferAttachment>,
        depthAttachment: IFramebufferAttachment?,
        width: Int,
        height: Int
    ) : this(location, glGenFramebuffers(), colorAttachments, depthAttachment, width, height)

    init {
        bind().use {
            colorAttachments.forEachIndexed { i, attachment ->
                attachment.resize2D(width, height)
                attachment.attach(GL_COLOR_ATTACHMENT0 + i, type().glName)
            }
            depthAttachment?.let { attachment ->
                attachment.resize2D(width, height)

                val depth = attachment.format().depth
                val stencil = attachment.format().stencil

                if (depth) {
                    attachment.attach(if (stencil) GL_DEPTH_STENCIL_ATTACHMENT else GL_DEPTH_ATTACHMENT, type().glName)
                } else {
                    if (!stencil) {
                        throw IllegalStateException("Illegal depth format ${attachment.format()} for framebuffer $location")
                    }

                    attachment.attach(GL_DEPTH_ATTACHMENT, type().glName)
                }
            }
        }
    }

    override fun bind(): Unbindable<NeoFramebuffer> {
        type().bind(id())
        return object : Unbindable<NeoFramebuffer> {
            override fun resource() = this@NeoFramebuffer

            override fun unbind() = Minecraft.getInstance().mainRenderTarget.bindWrite(true)
        }
    }

    override fun release() {
        glDeleteFramebuffers(id())
    }

    override fun location() = location

    override fun type() = GlResourceType.FRAMEBUFFER

    override fun id() = id

    override fun colorFormat() = colorAttachments[0].format()

    override fun depthFormat() = depthAttachment?.format()

    override fun width() = width

    override fun height() = height

    override fun resize(width: Int, height: Int) {
        bind().use {
            colorAttachments.forEach { it.resize2D(width, height) }
            depthAttachment?.resize2D(width, height)
        }
    }

    override fun clearColor(color: Vector4f) {
        glClearColor(color.x, color.y, color.z, color.w)
        glClear(GL_COLOR_BUFFER_BIT)
    }

    open class TextureBacked(
        location: ResourceLocation,
        colorFormats: Array<TextureFormat>,
        depthFormat: TextureFormat?,
        width: Int,
        height: Int,
    ) : NeoFramebuffer(
        location,
        colorFormats.mapIndexed { index, format ->
            NeoTexture(
                location.withSuffix("/color_$index"),
                GlResourceType.TEXTURE_2D,
                format
            )
        }.toTypedArray(),
        depthFormat?.let { format ->
            NeoTexture(
                location.withSuffix("/depth"),
                GlResourceType.TEXTURE_2D,
                format
            )
        },
        width,
        height
    )

    open class RenderBufferBacked(
        location: ResourceLocation,
        colorFormats: Array<TextureFormat>,
        depthFormat: TextureFormat?,
        width: Int,
        height: Int,
    ) : NeoFramebuffer(
        location,
        colorFormats.mapIndexed { index, format ->
            NeoRenderBuffer(
                location.withSuffix("/color_$index"),
                format
            )
        }.toTypedArray(),
        depthFormat?.let { format ->
            NeoRenderBuffer(
                location.withSuffix("/depth"),
                format
            )
        },
        width,
        height
    )
}