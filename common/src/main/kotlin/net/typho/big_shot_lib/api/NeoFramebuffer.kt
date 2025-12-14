package net.typho.big_shot_lib.api

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.GlResourceType
import net.typho.big_shot_lib.gl.TextureFormat
import net.typho.big_shot_lib.gl.Unbindable
import org.lwjgl.opengl.GL30.*

open class NeoFramebuffer(
    protected val location: ResourceLocation,
    protected val id: Int,
    val colorAttachments: Array<IFramebufferAttachment>,
    val depthAttachment: IFramebufferAttachment?,
    protected var width: Int,
    protected var height: Int
) : IFramebuffer {
    init {
        bind().use {
            colorAttachments.forEachIndexed { i, attachment -> attachment.attach(GL_COLOR_ATTACHMENT0 + i, type().glName) }
            depthAttachment?.let {
                val depth = it.format().depth
                val stencil = it.format().stencil

                if (depth) {
                    it.attach(if (stencil) GL_DEPTH_STENCIL_ATTACHMENT else GL_DEPTH_ATTACHMENT, type().glName)
                } else {
                    if (!stencil) {
                        throw IllegalStateException("Illegal depth format ${it.format()} for framebuffer $location")
                    }

                    it.attach(GL_DEPTH_ATTACHMENT, type().glName)
                }
            }
        }
    }

    override fun bind(): Unbindable {
        type().bind(id())
        return Unbindable.of(this)
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

    open class TextureBacked(
        location: ResourceLocation,
        colorFormats: Array<TextureFormat>,
        depthFormat: TextureFormat?,
        width: Int,
        height: Int,
    ) : NeoFramebuffer(
        location,
        glGenFramebuffers(),
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
        glGenFramebuffers(),
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