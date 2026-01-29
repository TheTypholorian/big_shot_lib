package net.typho.big_shot_lib.windows

import net.minecraft.client.DeltaTracker
import net.minecraft.client.renderer.MultiBufferSource
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.framebuffers.NeoFramebuffer
import net.typho.big_shot_lib.textures.TextureFormat
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
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
        /*
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
                    BigShotLib.SCREEN_VBO.bind()
                    BigShotLib.SCREEN_VBO.draw()
                    VertexBuffer.unbind()
                }
            //}
        //}
         */
    }
}