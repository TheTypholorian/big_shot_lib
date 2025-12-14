package net.typho.big_shot_lib

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexBuffer
import com.mojang.blaze3d.vertex.VertexFormat
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.typho.big_shot_lib.api.NeoShader

object BigShotLibFabric : ModInitializer {
    var vbo: VertexBuffer? = null

    override fun onInitialize() {
        RenderSystem.recordRenderCall {
            val vbo = VertexBuffer(VertexBuffer.Usage.STATIC)
            val builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION)

            builder.addVertex(0f, 0f, 0f)
            builder.addVertex(1f, 0f, 0f)
            builder.addVertex(1f, 1f, 0f)
            builder.addVertex(0f, 1f, 0f)

            vbo.bind()
            vbo.upload(builder.buildOrThrow())
            VertexBuffer.unbind()
            BigShotLibFabric.vbo = vbo
        }

        BigShotLib.init()
        WorldRenderEvents.LAST.register { context ->
            NeoShader.REGISTRY.get(BigShotLib.id("test_shader"))!!.bind().use {
                vbo!!.bind()
                vbo!!.draw()
                VertexBuffer.unbind()
            }
        }
    }
}