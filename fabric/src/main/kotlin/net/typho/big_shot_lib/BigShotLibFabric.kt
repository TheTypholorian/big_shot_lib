package net.typho.big_shot_lib

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexBuffer
import com.mojang.blaze3d.vertex.VertexFormat
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.Minecraft
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.phys.AABB
import net.typho.big_shot_lib.BigShotLib.cube
import net.typho.big_shot_lib.api.impl.NeoShader
import net.typho.big_shot_lib.gl.GlStack

object BigShotLibFabric : ClientModInitializer {
    val vbo by lazy {
        val builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
        builder.cube(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))

        val buffer = VertexBuffer(VertexBuffer.Usage.STATIC)

        buffer.bind()
        buffer.upload(builder.buildOrThrow())
        VertexBuffer.unbind()

        return@lazy buffer
    }

    override fun onInitializeClient() {
        BigShotLib.init()
        WorldRenderEvents.LAST.register {
            GlStack().use { stack ->
                val shader = NeoShader.get(BigShotLib.id("test"))!!
                shader.bind(stack)
                shader.setCommonUniforms()
                shader.setSampler("Sampler0", Minecraft.getInstance().textureManager.getTexture(InventoryMenu.BLOCK_ATLAS))

                vbo.bind()
                vbo.draw()
                VertexBuffer.unbind()
            }
        }
    }
}