package net.typho.big_shot_lib

import com.mojang.blaze3d.systems.RenderSystem
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
import net.typho.big_shot_lib.api.NeoFramebuffer
import net.typho.big_shot_lib.api.NeoShader
import net.typho.big_shot_lib.gl.TextureFormat
import org.joml.Vector4f

object BigShotLibFabric : ClientModInitializer {
    var cubeVBO: VertexBuffer? = null
    var fbo: NeoFramebuffer.TextureBacked? = null

    override fun onInitializeClient() {
        RenderSystem.recordRenderCall {
            val minecraft = Minecraft.getInstance()

            val cubeVBO = VertexBuffer(VertexBuffer.Usage.STATIC)
            val cubeBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)

            cubeBuilder.cube(AABB(0.0, 0.0, 0.0, 16.0, 16.0, 16.0))

            cubeVBO.bind()
            cubeVBO.upload(cubeBuilder.buildOrThrow())
            VertexBuffer.unbind()
            BigShotLibFabric.cubeVBO = cubeVBO

            val fbo = NeoFramebuffer.TextureBacked(
                BigShotLib.id("test_fbo"),
                arrayOf(TextureFormat.RGBA),
                null,
                minecraft.window.width,
                minecraft.window.height
            )
            NeoFramebuffer.AUTO_RESIZE.add(fbo)
            NeoFramebuffer.AUTO_CLEAR.put(fbo, Vector4f(0f))
            NeoFramebuffer.register(fbo)
            BigShotLibFabric.fbo = fbo
        }

        BigShotLib.init()
        WorldRenderEvents.LAST.register { context ->
            NeoShader.get(BigShotLib.id("test_shader"))!!.bind().use { shader ->
                fbo!!.bind().use {
                    val shader = shader.resource()
                    shader.setCommonUniforms()
                    shader.setSampler(
                        "TestSampler",
                        Minecraft.getInstance().modelManager.getAtlas(InventoryMenu.BLOCK_ATLAS)
                    )
                    cubeVBO!!.bind()
                    cubeVBO!!.draw()
                    VertexBuffer.unbind()
                }
            }
        }
    }
}