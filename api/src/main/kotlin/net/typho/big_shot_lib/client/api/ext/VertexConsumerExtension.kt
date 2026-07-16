package net.typho.big_shot_lib.client.api.ext

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter
import net.typho.big_shot_lib.client.api.rendering.util.PackedNormal
import net.typho.big_shot_lib.client.api.rendering.util.mesh.PrimitiveVertex
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

    fun addVertex(
        pos: IVec3<Float>,
        color: NeoColor? = null,
        textureUV: IVec2<Float>? = null,
        overlayUV: IVec2<Int>? = null,
        lightUV: IVec2<Int>? = null,
        normal: IVec3<Float>? = null
    ): VertexConsumer {
        addVertex(pos)
        color?.let { setColor(it) }
        textureUV?.let { setUv(it) }
        overlayUV?.let { setUv1(it) }
        lightUV?.let { setUv2(it) }
        normal?.let { setNormal(it) }
        return self()
    }

    fun addVertex(vertex: PrimitiveVertex): VertexConsumer {
        return self().addVertex(vertex.x, vertex.y, vertex.z)
            .setColor(vertex.color)
            .setUv(vertex.u, vertex.v)
            .setLight(vertex.light)
            .setNormal(vertex.normal)
    }

    fun addVertex(pose: PoseStack.Pose, vertex: PrimitiveVertex): VertexConsumer {
        return self().addVertex(pose, vertex.x, vertex.y, vertex.z)
            .setColor(vertex.color)
            .setUv(vertex.u, vertex.v)
            .setLight(vertex.light)
            .setNormal(pose, vertex.normal)
    }

    fun addVertex(packed: IntArray, offset: Int): VertexConsumer {
        return self().addVertex(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1]),
            Float.fromBits(packed[offset + 2])
        )
    }

    fun addVertex(vertex: IVec3<*>): VertexConsumer {
        return self().addVertex(vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
    }

    fun addVertex(mat: Matrix4fc, vertex: IVec3<*>): VertexConsumer {
        return addVertex(mat, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
    }

    /**
     * Mutates the vertex param
     */
    fun addVertex(mat: Matrix4fc, vertex: Vector3f): VertexConsumer {
        mat.transformPosition(vertex, Vector3f())
        return self().addVertex(vertex.x, vertex.y, vertex.z)
    }

    fun addVertex(mat: Matrix4fc, x: Float, y: Float, z: Float): VertexConsumer {
        val vertex = mat.transformPosition(Vector3f(x, y, z))
        return self().addVertex(vertex.x, vertex.y, vertex.z)
    }

    fun addVertex(pose: PoseStack.Pose, vertex: IVec3<*>): VertexConsumer {
        return self().addVertex(pose, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
    }

    fun setColor(argb: Int): VertexConsumer {
        return self().setColor(argb)
    }

    fun setColor(packed: IntArray, offset: Int): VertexConsumer {
        return self().setColor(packed[offset])
    }

    fun setColor(color: NeoColor): VertexConsumer {
        return self().setColor(color.red.toInt(), color.green.toInt(), color.blue.toInt(), color.alpha?.toInt() ?: 255)
    }

    fun setUv(packed: IntArray, offset: Int): VertexConsumer {
        return self().setUv(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1])
        )
    }

    fun setUv(uv: IVec2<Float>): VertexConsumer {
        return self().setUv(uv.x, uv.y)
    }

    fun setUv1(packed: IntArray, offset: Int): VertexConsumer {
        return self().setOverlay(packed[offset])
    }

    fun setUv1(uv: IVec2<Int>): VertexConsumer {
        return self().setUv1(uv.x, uv.y)
    }

    fun setUv2(packed: IntArray, offset: Int): VertexConsumer {
        return self().setLight(packed[offset])
    }

    fun setUv2(uv: IVec2<Int>): VertexConsumer {
        return self().setUv2(uv.x, uv.y)
    }

    fun setNormal(packed: IntArray, offset: Int): VertexConsumer {
        setNormal(packed[offset])
        return self()
    }

    fun setNormal(normal: IVec3<*>): VertexConsumer {
        return self().setNormal(normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
    }

    fun setNormal(x: Byte, y: Byte, z: Byte): VertexConsumer {
        return self().setNormal(x / 127f, y / 127f, z / 127f)
    }

    fun setNormal(packed: Int): VertexConsumer {
        return self().setNormal(PackedNormal.unpackByteX(packed), PackedNormal.unpackByteY(packed), PackedNormal.unpackByteZ(packed))
    }

    fun setNormal(pose: PoseStack.Pose, normal: IVec3<*>): VertexConsumer {
        return self().setNormal(pose, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
    }

    fun setNormal(pose: PoseStack.Pose, packed: Int): VertexConsumer {
        return self().setNormal(pose, PackedNormal.unpackX(packed), PackedNormal.unpackY(packed), PackedNormal.unpackZ(packed))
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
        addVertex(v0).setUv(0f, 1f).setNormal(normal)
        addVertex(v1).setUv(1f, 1f).setNormal(normal)
        addVertex(v2).setUv(1f, 0f).setNormal(normal)
        addVertex(v3).setUv(0f, 0f).setNormal(normal)

        return self()
    }
}