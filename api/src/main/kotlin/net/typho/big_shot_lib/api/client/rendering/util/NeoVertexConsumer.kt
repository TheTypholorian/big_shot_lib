package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.vertex.PoseStack
import net.typho.big_shot_lib.api.client.InternalClientUtil
import net.typho.big_shot_lib.api.math.IRect3
import net.typho.big_shot_lib.api.math.IVec2
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.util.NeoColor
import org.joml.Matrix4fc
import org.joml.Vector3f

interface NeoVertexConsumer {
    fun vertex(x: Float, y: Float, z: Float): NeoVertexConsumer

    fun color(r: Int, g: Int, b: Int, a: Int): NeoVertexConsumer

    fun textureUV(u: Float, v: Float): NeoVertexConsumer

    fun overlayUV(u: Int, v: Int): NeoVertexConsumer

    fun lightUV(u: Int, v: Int): NeoVertexConsumer

    fun normal(x: Float, y: Float, z: Float): NeoVertexConsumer

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

    fun vertex(packed: IntArray, offset: Int): NeoVertexConsumer {
        vertex(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1]),
            Float.fromBits(packed[offset + 2])
        )
        return this
    }

    fun vertex(vertex: IVec3<*>): NeoVertexConsumer {
        vertex(vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this
    }

    fun vertex(mat: Matrix4fc, x: Float, y: Float, z: Float): NeoVertexConsumer {
        val transformed = mat.transformPosition(x, y, z, Vector3f())
        vertex(transformed.x, transformed.y, transformed.z)
        return this
    }

    fun vertex(mat: Matrix4fc, vertex: IVec3<*>): NeoVertexConsumer {
        vertex(mat, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this
    }

    fun vertex(pose: PoseStack.Pose, x: Float, y: Float, z: Float): NeoVertexConsumer {
        vertex(pose.pose(), x, y, z)
        return this
    }

    fun vertex(pose: PoseStack.Pose, vertex: IVec3<*>): NeoVertexConsumer {
        vertex(pose, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this
    }

    fun color(packed: IntArray, offset: Int): NeoVertexConsumer {
        color(packed[offset])
        return this
    }

    fun color(r: Float, g: Float, b: Float, a: Float): NeoVertexConsumer {
        color((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), (a * 255).toInt())
        return this
    }

    fun color(color: NeoColor): NeoVertexConsumer {
        color(color.red.toInt(), color.green.toInt(), color.blue.toInt(), color.alpha?.toInt() ?: 255)
        return this
    }

    fun color(argb: Int): NeoVertexConsumer {
        color(argb ushr 16 and 0xFF, argb ushr 8 and 0xFF, argb and 0xFF, argb ushr 24)
        return this
    }

    fun textureUV(packed: IntArray, offset: Int): NeoVertexConsumer {
        textureUV(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1])
        )
        return this
    }

    fun textureUV(uv: IVec2<Float>): NeoVertexConsumer {
        textureUV(uv.x, uv.y)
        return this
    }

    fun overlayUV(packed: IntArray, offset: Int): NeoVertexConsumer {
        overlayUV(packed[offset])
        return this
    }

    fun overlayUV(uv: IVec2<Int>): NeoVertexConsumer {
        overlayUV(uv.x, uv.y)
        return this
    }

    fun overlayUV(packed: Int): NeoVertexConsumer {
        overlayUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this
    }

    fun lightUV(packed: IntArray, offset: Int): NeoVertexConsumer {
        lightUV(packed[offset])
        return this
    }

    fun lightUV(uv: IVec2<Int>): NeoVertexConsumer {
        lightUV(uv.x, uv.y)
        return this
    }

    fun lightUV(packed: Int): NeoVertexConsumer {
        lightUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this
    }

    fun normal(packed: IntArray, offset: Int): NeoVertexConsumer {
        normal(packed[offset])
        return this
    }

    fun normal(normal: IVec3<*>): NeoVertexConsumer {
        normal(normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
        return this
    }

    fun normal(packed: Int): NeoVertexConsumer {
        normal(
            (packed ushr 24) / 127f,
            ((packed ushr 16) and 0xFF) / 127f,
            ((packed ushr 8) and 0xFF) / 127f
        )
        return this
    }

    fun normal(pose: PoseStack.Pose, x: Float, y: Float, z: Float): NeoVertexConsumer {
        normal(InternalClientUtil.INSTANCE.transformNormal(pose, x, y, z))
        return this
    }

    fun normal(pose: PoseStack.Pose, normal: IVec3<*>): NeoVertexConsumer {
        normal(pose, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
        return this
    }

    fun cube(
        box: IRect3<*>,
    ): NeoVertexConsumer {
        val vertices = arrayOf(
            IVec3(box.max.x.toFloat(), box.max.x.toFloat(), box.max.x.toFloat()),
            IVec3(box.min.x.toFloat(), box.max.x.toFloat(), box.max.x.toFloat()),
            IVec3(box.min.x.toFloat(), box.min.x.toFloat(), box.max.x.toFloat()),
            IVec3(box.max.x.toFloat(), box.min.x.toFloat(), box.max.x.toFloat()),
            IVec3(box.max.x.toFloat(), box.max.x.toFloat(), box.min.x.toFloat()),
            IVec3(box.min.x.toFloat(), box.max.x.toFloat(), box.min.x.toFloat()),
            IVec3(box.min.x.toFloat(), box.min.x.toFloat(), box.min.x.toFloat()),
            IVec3(box.max.x.toFloat(), box.min.x.toFloat(), box.min.x.toFloat()),
        )

        quad(vertices[0], vertices[1], vertices[2], vertices[3], IVec3(0f, 0f, 1f))
        quad(vertices[1], vertices[5], vertices[6], vertices[2], IVec3(-1f, 0f, 0f))
        quad(vertices[5], vertices[4], vertices[7], vertices[6], IVec3(0f, 0f, -1f))
        quad(vertices[4], vertices[0], vertices[3], vertices[7], IVec3(1f, 0f, 0f))
        quad(vertices[1], vertices[0], vertices[4], vertices[5], IVec3(0f, 1f, 0f))
        quad(vertices[3], vertices[2], vertices[6], vertices[7], IVec3(0f, -1f, 0f))

        return this
    }

    fun quad(
        v0: IVec3<*>,
        v1: IVec3<*>,
        v2: IVec3<*>,
        v3: IVec3<*>,
        normal: IVec3<*>
    ): NeoVertexConsumer {
        vertex(v0).textureUV(0f, 1f).normal(normal)
        vertex(v1).textureUV(1f, 1f).normal(normal)
        vertex(v2).textureUV(1f, 0f).normal(normal)
        vertex(v3).textureUV(0f, 0f).normal(normal)

        return this
    }
}