package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.math.rect.IRect3
import net.typho.big_shot_lib.api.math.vec.IVec2
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.plugin.Namespace
import net.typho.big_shot_lib.api.util.NeoColor
import org.joml.Matrix4fc
import org.joml.Vector3f

@Namespace(BigShotApi.MOD_ID)
interface NeoVertexConsumer {
    fun vertex(x: Float, y: Float, z: Float): VertexConsumer

    fun color(r: Int, g: Int, b: Int, a: Int): VertexConsumer

    fun textureUV(u: Float, v: Float): VertexConsumer

    fun overlayUV(u: Int, v: Int): VertexConsumer

    fun lightUV(u: Int, v: Int): VertexConsumer

    fun normal(x: Float, y: Float, z: Float): VertexConsumer

    /**
     * TODO: Remove when 1.20.x support is dropped
     */
    fun end()

    fun vertex(
        pos: IVec3<Float>,
        color: NeoColor? = null,
        textureUV: IVec2<Float>? = null,
        overlayUV: IVec2<Int>? = null,
        lightUV: IVec2<Int>? = null,
        normal: IVec3<Float>? = null
    ) {
        vertex(pos)
        color?.let { color(it) }
        textureUV?.let { textureUV(it) }
        overlayUV?.let { overlayUV(it) }
        lightUV?.let { lightUV(it) }
        normal?.let { normal(it) }
        end()
    }

    fun vertex(packed: IntArray, offset: Int): VertexConsumer {
        vertex(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1]),
            Float.fromBits(packed[offset + 2])
        )
        return this as VertexConsumer
    }

    fun vertex(vertex: IVec3<*>): VertexConsumer {
        vertex(vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this as VertexConsumer
    }

    fun vertex(mat: Matrix4fc, x: Float, y: Float, z: Float): VertexConsumer {
        val transformed = mat.transformPosition(x, y, z, Vector3f())
        vertex(transformed.x, transformed.y, transformed.z)
        return this as VertexConsumer
    }

    fun vertex(mat: Matrix4fc, vertex: IVec3<*>): VertexConsumer {
        vertex(mat, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this as VertexConsumer
    }

    fun vertex(pose: PoseStack.Pose, x: Float, y: Float, z: Float): VertexConsumer {
        vertex(pose.pose(), x, y, z)
        return this as VertexConsumer
    }

    fun vertex(pose: PoseStack.Pose, vertex: IVec3<*>): VertexConsumer {
        vertex(pose, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this as VertexConsumer
    }

    fun color(packed: IntArray, offset: Int): VertexConsumer {
        color(packed[offset])
        return this as VertexConsumer
    }

    fun color(r: Float, g: Float, b: Float, a: Float): VertexConsumer {
        color((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), (a * 255).toInt())
        return this as VertexConsumer
    }

    fun color(color: NeoColor): VertexConsumer {
        color(color.red.toInt(), color.green.toInt(), color.blue.toInt(), color.alpha?.toInt() ?: 255)
        return this as VertexConsumer
    }

    fun color(argb: Int): VertexConsumer {
        color(argb ushr 16 and 0xFF, argb ushr 8 and 0xFF, argb and 0xFF, argb ushr 24)
        return this as VertexConsumer
    }

    fun textureUV(packed: IntArray, offset: Int): VertexConsumer {
        textureUV(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1])
        )
        return this as VertexConsumer
    }

    fun textureUV(uv: IVec2<Float>): VertexConsumer {
        textureUV(uv.x, uv.y)
        return this as VertexConsumer
    }

    fun overlayUV(packed: IntArray, offset: Int): VertexConsumer {
        overlayUV(packed[offset])
        return this as VertexConsumer
    }

    fun overlayUV(uv: IVec2<Int>): VertexConsumer {
        overlayUV(uv.x, uv.y)
        return this as VertexConsumer
    }

    fun overlayUV(packed: Int): VertexConsumer {
        overlayUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this as VertexConsumer
    }

    fun lightUV(packed: IntArray, offset: Int): VertexConsumer {
        lightUV(packed[offset])
        return this as VertexConsumer
    }

    fun lightUV(uv: IVec2<Int>): VertexConsumer {
        lightUV(uv.x, uv.y)
        return this as VertexConsumer
    }

    fun lightUV(packed: Int): VertexConsumer {
        lightUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this as VertexConsumer
    }

    fun normal(packed: IntArray, offset: Int): VertexConsumer {
        normal(packed[offset])
        return this as VertexConsumer
    }

    fun normal(normal: IVec3<*>): VertexConsumer {
        normal(normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
        return this as VertexConsumer
    }

    fun normal(packed: Int): VertexConsumer {
        normal(
            (packed ushr 24) / 127f,
            ((packed ushr 16) and 0xFF) / 127f,
            ((packed ushr 8) and 0xFF) / 127f
        )
        return this as VertexConsumer
    }

    fun normal(pose: PoseStack.Pose, x: Float, y: Float, z: Float): VertexConsumer {
        normal(InternalUtil.INSTANCE.transformNormal(pose, x, y, z))
        return this as VertexConsumer
    }

    fun normal(pose: PoseStack.Pose, normal: IVec3<*>): VertexConsumer {
        normal(pose, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
        return this as VertexConsumer
    }

    fun cube(
        box: IRect3<*>,
    ): VertexConsumer {
        val vertices = arrayOf(
            NeoVec3f(box.max.x.toFloat(), box.max.x.toFloat(), box.max.x.toFloat()),
            NeoVec3f(box.min.x.toFloat(), box.max.x.toFloat(), box.max.x.toFloat()),
            NeoVec3f(box.min.x.toFloat(), box.min.x.toFloat(), box.max.x.toFloat()),
            NeoVec3f(box.max.x.toFloat(), box.min.x.toFloat(), box.max.x.toFloat()),
            NeoVec3f(box.max.x.toFloat(), box.max.x.toFloat(), box.min.x.toFloat()),
            NeoVec3f(box.min.x.toFloat(), box.max.x.toFloat(), box.min.x.toFloat()),
            NeoVec3f(box.min.x.toFloat(), box.min.x.toFloat(), box.min.x.toFloat()),
            NeoVec3f(box.max.x.toFloat(), box.min.x.toFloat(), box.min.x.toFloat()),
        )

        quad(vertices[0], vertices[1], vertices[2], vertices[3], NeoVec3f(0f, 0f, 1f))
        quad(vertices[1], vertices[5], vertices[6], vertices[2], NeoVec3f(-1f, 0f, 0f))
        quad(vertices[5], vertices[4], vertices[7], vertices[6], NeoVec3f(0f, 0f, -1f))
        quad(vertices[4], vertices[0], vertices[3], vertices[7], NeoVec3f(1f, 0f, 0f))
        quad(vertices[1], vertices[0], vertices[4], vertices[5], NeoVec3f(0f, 1f, 0f))
        quad(vertices[3], vertices[2], vertices[6], vertices[7], NeoVec3f(0f, -1f, 0f))

        return this as VertexConsumer
    }

    fun quad(
        v0: IVec3<*>,
        v1: IVec3<*>,
        v2: IVec3<*>,
        v3: IVec3<*>,
        normal: IVec3<*>
    ): VertexConsumer {
        vertex(v0).textureUV(0f, 1f).normal(normal)
        vertex(v1).textureUV(1f, 1f).normal(normal)
        vertex(v2).textureUV(1f, 0f).normal(normal)
        vertex(v3).textureUV(0f, 0f).normal(normal)

        return this as VertexConsumer
    }
}