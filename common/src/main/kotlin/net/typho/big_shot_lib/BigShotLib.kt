package net.typho.big_shot_lib

import com.mojang.blaze3d.buffers.BufferUsage
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.AABB
import org.joml.Matrix4f
import org.joml.Vector3f
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotLib {
    const val MOD_ID = "big_shot_lib"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")
    val SCREEN_VBO: VertexBuffer by lazy {
        val vbo = VertexBuffer(BufferUsage.STATIC_WRITE)
        val blitBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)

        blitBuilder.quad(
            Vector3f(-1f, 1f, 0f),
            Vector3f(1f, 1f, 0f),
            Vector3f(1f, -1f, 0f),
            Vector3f(-1f, -1f, 0f),
            Vector3f(0f, 0f, -1f)
        )

        vbo.bind()
        vbo.upload(blitBuilder.buildOrThrow())
        VertexBuffer.unbind()
        return@lazy vbo
    }

    fun init() = Unit

    @JvmStatic
    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

    @JvmStatic
    fun getViewMatrix(
        camera: Camera = Minecraft.getInstance().gameRenderer.mainCamera,
        matrix: Matrix4f = RenderSystem.getModelViewMatrix()
    ): Matrix4f {
        return matrix.translate(camera.position.toVector3f().mul(-1f))
    }

    @JvmStatic
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

    @JvmStatic
    fun VertexConsumer.quad(
        v0: Vector3f,
        v1: Vector3f,
        v2: Vector3f,
        v3: Vector3f,
        normal: Vector3f
    ) {
        addVertex(v0).setUv(0f, 1f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v1).setUv(1f, 1f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v2).setUv(1f, 0f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v3).setUv(0f, 0f).setNormal(normal.x, normal.y, normal.z)
    }
}