package net.typho.big_shot_lib

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.AABB
import net.typho.big_shot_lib.api.IWindow
import net.typho.big_shot_lib.api.NeoWindow
import org.joml.Matrix4f
import org.joml.Vector3f
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotLib {
    const val MOD_ID = "big_shot_lib"
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")
    var DEBUG_WINDOW: NeoWindow? = null
    var blitVBO: VertexBuffer? = null

    fun init() {
        RenderSystem.recordRenderCall {
            val blitVBO = VertexBuffer(VertexBuffer.Usage.STATIC)
            val blitBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)

            blitBuilder.quad(
                Vector3f(0f, 0f, 0f),
                Vector3f(1f, 0f, 0f),
                Vector3f(1f, 1f, 0f),
                Vector3f(0f, 1f, 0f),
                Vector3f(0f, 0f, -1f)
            )

            blitVBO.bind()
            blitVBO.upload(blitBuilder.buildOrThrow())
            VertexBuffer.unbind()
            BigShotLib.blitVBO = blitVBO

            val window = DebugWindow(
                IWindow.create(1280, 800, "Debug Window"),
                MultiBufferSource.immediate(ByteBufferBuilder(1536))
            )
            NeoWindow.AUTO_RENDER.add(window)
            DEBUG_WINDOW = window
        }
    }

    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

    fun getViewMatrix(
        camera: Camera = Minecraft.getInstance().gameRenderer.mainCamera,
        matrix: Matrix4f = RenderSystem.getModelViewMatrix()
    ): Matrix4f {
        return matrix.translate(camera.position.toVector3f().mul(-1f))
    }

    fun VertexConsumer.cube(
        box: AABB,
    ) {
        val vertices = arrayOf(
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),
        )

        quad(vertices[0], vertices[1], vertices[2], vertices[3], Vector3f(0f, 0f, 1f))
        quad(vertices[1], vertices[5], vertices[6], vertices[2], Vector3f(-1f, 0f, 0f))
        quad(vertices[5], vertices[4], vertices[7], vertices[6], Vector3f(0f, 0f, -1f))
        quad(vertices[4], vertices[0], vertices[3], vertices[7], Vector3f(1f, 0f, 0f))
        quad(vertices[1], vertices[0], vertices[4], vertices[5], Vector3f(0f, 1f, 0f))
        quad(vertices[3], vertices[2], vertices[6], vertices[7], Vector3f(0f, -1f, 0f))
    }

    fun VertexConsumer.quad(
        v0: Vector3f,
        v1: Vector3f,
        v2: Vector3f,
        v3: Vector3f,
        normal: Vector3f
    ) {
        addVertex(v0).setUv(1f, 0f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v1).setUv(0f, 0f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v2).setUv(0f, 1f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v3).setUv(1f, 1f).setNormal(normal.x, normal.y, normal.z)
    }
}