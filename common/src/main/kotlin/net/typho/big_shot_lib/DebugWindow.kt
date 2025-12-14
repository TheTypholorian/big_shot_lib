package net.typho.big_shot_lib

import com.mojang.blaze3d.vertex.VertexBuffer
import net.minecraft.client.DeltaTracker
import net.minecraft.client.renderer.MultiBufferSource
import net.typho.big_shot_lib.api.NeoFramebuffer
import net.typho.big_shot_lib.api.NeoShader
import net.typho.big_shot_lib.api.NeoWindow
import net.typho.big_shot_lib.gl.TextureFormat

class DebugWindow(
    handle: Long,
    bufferSource: MultiBufferSource.BufferSource
) : NeoWindow(handle, 1280, 800, NeoFramebuffer.TextureBacked(
    BigShotLib.id("debug_window"),
    arrayOf(TextureFormat.RGBA),
    null,
    1280,
    800
), bufferSource) {
    override fun render(deltaTracker: DeltaTracker) {
        //framebuffer().bind().use { fbo ->
        //    val fbo = fbo.resource()
            //NeoFramebuffer.REGISTRY.get(BigShotLib.id("test_fbo"))?.let { testFbo ->
                NeoShader.get(BigShotLib.id("blit"))!!.bind().use { shader ->
                    val shader = shader.resource()
                    shader.setCommonUniforms()
                    //shader.setSampler("Sampler0", testFbo.colorAttachments[0] as ITexture)
                    shader.getUniform("Width")?.set(1.5f)
                    shader.getUniform("Height")?.set(1f)
                    shader.getUniform("X")?.set(-0.75f)
                    shader.getUniform("Y")?.set(-0.5f)
                    BigShotLib.blitVBO!!.bind()
                    BigShotLib.blitVBO!!.draw()
                    VertexBuffer.unbind()
                }
            //}
        //}
    }
}