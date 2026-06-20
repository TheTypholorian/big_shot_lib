package net.typho.big_shot_lib.api.client.ext

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter
import net.typho.big_shot_lib.api.client.rendering.util.PackedNormal
import net.typho.big_shot_lib.api.client.rendering.util.quad.PrimitiveVertex
import net.typho.big_shot_lib.api.math.IRect3
import net.typho.big_shot_lib.api.math.IVec2
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.buffer.MemoryWriter
import org.joml.Matrix4fc
import org.joml.Vector3f
import java.util.function.Consumer

interface VertexConsumerExtension : VertexBufferWriter {
    @Suppress("NOTHING_TO_INLINE")
    private inline fun self() = this as VertexConsumer

    fun vertex(
        pos: IVec3<Float>,
        color: NeoColor? = null,
        textureUV: IVec2<Float>? = null,
        overlayUV: IVec2<Int>? = null,
        lightUV: IVec2<Int>? = null,
        normal: IVec3<Float>? = null
    ): VertexConsumer {
        vertex(pos)
        color?.let { color(it) }
        textureUV?.let { textureUV(it) }
        overlayUV?.let { overlayUV(it) }
        lightUV?.let { lightUV(it) }
        normal?.let { normal(it) }
        return self()
    }

    fun vertex(vertex: PrimitiveVertex): VertexConsumer {
        return self().vertex(vertex.x, vertex.y, vertex.z)
            .color(vertex.color)
            .textureUV(vertex.u, vertex.v)
            .lightUV(vertex.light)
            .normal(vertex.normal)
    }

    fun vertex(pose: PoseStack.Pose, vertex: PrimitiveVertex): VertexConsumer {
        return self().vertex(pose, vertex.x, vertex.y, vertex.z)
            .color(vertex.color)
            .textureUV(vertex.u, vertex.v)
            .lightUV(vertex.light)
            .normal(pose, vertex.normal)
    }

    fun vertex(packed: IntArray, offset: Int): VertexConsumer {
        return self().vertex(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1]),
            Float.fromBits(packed[offset + 2])
        )
    }

    fun vertex(vertex: IVec3<*>): VertexConsumer {
        return self().vertex(vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
    }

    fun vertex(mat: Matrix4fc, vertex: IVec3<*>): VertexConsumer {
        return vertex(mat, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
    }

    /**
     * Mutates the vertex param
     */
    fun vertex(mat: Matrix4fc, vertex: Vector3f): VertexConsumer {
        mat.transformPosition(vertex, Vector3f())
        return self().vertex(vertex.x, vertex.y, vertex.z)
    }

    fun vertex(mat: Matrix4fc, x: Float, y: Float, z: Float): VertexConsumer {
        val vertex = mat.transformPosition(Vector3f(x, y, z))
        return self().vertex(vertex.x, vertex.y, vertex.z)
    }

    fun vertex(pose: PoseStack.Pose, vertex: IVec3<*>): VertexConsumer {
        return self().vertex(pose, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
    }

    fun color(packed: IntArray, offset: Int): VertexConsumer {
        return self().color(packed[offset])
    }

    fun color(color: NeoColor): VertexConsumer {
        return self().color(color.red.toInt(), color.green.toInt(), color.blue.toInt(), color.alpha?.toInt() ?: 255)
    }

    fun textureUV(packed: IntArray, offset: Int): VertexConsumer {
        return self().textureUV(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1])
        )
    }

    fun textureUV(uv: IVec2<Float>): VertexConsumer {
        return self().textureUV(uv.x, uv.y)
    }

    fun overlayUV(packed: IntArray, offset: Int): VertexConsumer {
        return self().overlayUV(packed[offset])
    }

    fun overlayUV(uv: IVec2<Int>): VertexConsumer {
        return self().overlayUV(uv.x, uv.y)
    }

    fun lightUV(packed: IntArray, offset: Int): VertexConsumer {
        return self().lightUV(packed[offset])
    }

    fun lightUV(uv: IVec2<Int>): VertexConsumer {
        return self().lightUV(uv.x, uv.y)
    }

    fun normal(packed: IntArray, offset: Int): VertexConsumer {
        normal(packed[offset])
        return self()
    }

    fun normal(normal: IVec3<*>): VertexConsumer {
        return self().normal(normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
    }

    fun normal(x: Byte, y: Byte, z: Byte): VertexConsumer {
        return self().normal(x / 127f, y / 127f, z / 127f)
    }

    fun normal(packed: Int): VertexConsumer {
        return self().normal(PackedNormal.unpackByteX(packed), PackedNormal.unpackByteY(packed), PackedNormal.unpackByteZ(packed))
    }

    fun normal(pose: PoseStack.Pose, normal: IVec3<*>): VertexConsumer {
        return self().normal(pose, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
    }

    fun normal(pose: PoseStack.Pose, packed: Int): VertexConsumer {
        return self().normal(pose, PackedNormal.unpackX(packed), PackedNormal.unpackY(packed), PackedNormal.unpackZ(packed))
    }

    fun customElementsSupported() = false

    fun custom(element: VertexFormatElement, out: Consumer<MemoryWriter>): VertexConsumer {
        return self()
    }

    fun setLineWidth(width: Float): VertexConsumer

    fun cube(
        box: IRect3<*>,
    ): VertexConsumer {
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

        return self()
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

        return self()
    }
}