package net.typho.big_shot_lib.api.impl

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.typho.big_shot_lib.api.IFramebuffer
import net.typho.big_shot_lib.api.IWindow
import org.jetbrains.annotations.ApiStatus
import java.util.*

@ApiStatus.Experimental
abstract class NeoWindow(
    protected val handle: Long,
    protected var width: Int,
    protected var height: Int,
    protected val framebuffer: IFramebuffer,
    val bufferSource: MultiBufferSource.BufferSource
) : IWindow {
    companion object {
        val AUTO_RENDER = LinkedList<NeoWindow>()
    }

    override fun handle() = handle

    override fun width() = width

    override fun height() = height

    override fun framebuffer() = framebuffer

    override fun createGraphics() = NeoGuiGraphics(this, Minecraft.getInstance(), bufferSource)
}