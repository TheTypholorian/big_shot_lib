package net.typho.big_shot_lib.framebuffers

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.buffers.NeoRenderBuffer
import net.typho.big_shot_lib.error.IncompleteFramebufferException
import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.resource.TextureFormat
import net.typho.big_shot_lib.textures.NeoTexture
import org.joml.Vector4f
import org.lwjgl.opengl.GL30.*
import java.util.*

@OptIn(ExperimentalStdlibApi::class)
open class NeoFramebuffer(
    protected val location: ResourceLocation,
    protected val id: Int,
    val colorAttachments: Array<IFramebufferAttachment>,
    val depthAttachment: IFramebufferAttachment?,
    protected var width: Int,
    protected var height: Int
) : IFramebuffer {
    companion object {
        @JvmField
        val AUTO_RESIZE = LinkedList<NeoFramebuffer>()
        @JvmField
        val AUTO_CLEAR = LinkedHashMap<NeoFramebuffer, Vector4f>()
        @JvmField
        val REGISTRY = HashMap<ResourceLocation, NeoFramebuffer>()

        @JvmStatic
        fun register(framebuffer: NeoFramebuffer) {
            REGISTRY.put(framebuffer.location(), framebuffer)
        }

        @JvmStatic
        fun get(location: ResourceLocation) = REGISTRY.get(location)
    }

    constructor(
        location: ResourceLocation,
        colorAttachments: Array<IFramebufferAttachment>,
        depthAttachment: IFramebufferAttachment?,
        width: Int,
        height: Int
    ) : this(location, glGenFramebuffers(), colorAttachments, depthAttachment, width, height)

    init {
        bind()

        colorAttachments.forEachIndexed { i, attachment ->
            attachment.resize2D(width, height)
            attachment.attach2D(GL_COLOR_ATTACHMENT0 + i, type().glName)
        }
        depthAttachment?.let { attachment ->
            attachment.resize2D(width, height)

            val depth = attachment.format().depth
            val stencil = attachment.format().stencil

            when {
                depth && stencil -> attachment.attach2D(GL_DEPTH_STENCIL_ATTACHMENT, type().glName)
                depth && !stencil -> attachment.attach2D(GL_DEPTH_ATTACHMENT, type().glName)
                !depth && stencil -> attachment.attach2D(GL_STENCIL_ATTACHMENT, type().glName)
                else -> throw IllegalStateException("Illegal depth format ${attachment.format()} for framebuffer $location")
            }
        }

        val status = glCheckFramebufferStatus(type().glName)

        if (status != GL_FRAMEBUFFER_COMPLETE) {
            throw IncompleteFramebufferException("Framebuffer ${location()} incomplete with error 0x${status.toHexString()}")
        }

        unbind()

        type().label(id(), location().toString())
    }

    override fun unbind() {
        Minecraft.getInstance().mainRenderTarget.bindWrite(true)
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
        bind()

        colorAttachments.forEach { it.resize2D(width, height) }
        depthAttachment?.resize2D(width, height)

        unbind()

        this.width = width
        this.height = height
    }

    override fun clearColor(color: Vector4f) {
        GlStateManager._clearColor(color.x, color.y, color.z, color.w)
        GlStateManager._clear(GL_COLOR_BUFFER_BIT, false)
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