package net.typho.big_shot_lib

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexBuffer
import com.mojang.blaze3d.vertex.VertexFormat
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.Minecraft
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.phys.AABB
import net.typho.big_shot_lib.BigShotLib.cube
import net.typho.big_shot_lib.api.NeoShader

object BigShotLibFabric : ModInitializer {
    var vbo: VertexBuffer? = null

    override fun onInitialize() {
        RenderSystem.recordRenderCall {
            val vbo = VertexBuffer(VertexBuffer.Usage.STATIC)
            val builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)

            builder.cube(AABB(0.0, 0.0, 0.0, 16.0, 16.0, 16.0))

            vbo.bind()
            vbo.upload(builder.buildOrThrow())
            VertexBuffer.unbind()
            BigShotLibFabric.vbo = vbo
        }

        BigShotLib.init()
        WorldRenderEvents.LAST.register { context ->
            NeoShader.REGISTRY[BigShotLib.id("test_shader")]!!.bind().use {
                val shader = it.resource()
                shader.setCommonUniforms()
                shader.setSampler("TestSampler", Minecraft.getInstance().modelManager.getAtlas(InventoryMenu.BLOCK_ATLAS))
                vbo!!.bind()
                vbo!!.draw()
                VertexBuffer.unbind()
            }
        }
    }
}