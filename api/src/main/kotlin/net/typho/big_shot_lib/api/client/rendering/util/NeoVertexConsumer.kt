package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.typho.big_shot_lib.api.math.rect.AbstractRect3
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.NeoColor
import org.joml.Matrix4fc
import org.joml.Vector3f

abstract class NeoVertexConsumer {
    abstract fun vertex(x: Float, y: Float, z: Float): NeoVertexConsumer

    abstract fun color(r: Int, g: Int, b: Int, a: Int): NeoVertexConsumer

    abstract fun textureUV(u: Float, v: Float): NeoVertexConsumer

    abstract fun overlayUV(u: Int, v: Int): NeoVertexConsumer

    abstract fun lightUV(u: Int, v: Int): NeoVertexConsumer

    abstract fun normal(x: Float, y: Float, z: Float): NeoVertexConsumer

    abstract fun normal(pose: PoseStack.Pose, x: Float, y: Float, z: Float): NeoVertexConsumer

    /**
     * This method only exists for pre-1.21 compat, and will be removed once 1.20.x support is dropped.
     * The only place it is overwritten is in pre-1.21 WrapperUtil.wrap(VertexConsumer), accomplished with [_endVertex].
     */
    fun endVertex() {
        _endVertex()
    }

    internal open fun _endVertex() {
    }

    open fun vertex(packed: IntArray, offset: Int): NeoVertexConsumer {
        vertex(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1]),
            Float.fromBits(packed[offset + 2])
        )
        return this
    }

    open fun vertex(vertex: AbstractVec3<*>): NeoVertexConsumer {
        vertex(vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this
    }

    open fun vertex(mat: Matrix4fc, x: Float, y: Float, z: Float): NeoVertexConsumer {
        val transformed = mat.transformPosition(x, y, z, Vector3f())
        vertex(transformed.x, transformed.y, transformed.z)
        return this
    }

    open fun vertex(mat: Matrix4fc, vertex: AbstractVec3<*>): NeoVertexConsumer {
        vertex(mat, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this
    }

    open fun vertex(pose: PoseStack.Pose, x: Float, y: Float, z: Float): NeoVertexConsumer {
        vertex(pose.pose(), x, y, z)
        return this
    }

    open fun vertex(pose: PoseStack.Pose, vertex: AbstractVec3<*>): NeoVertexConsumer {
        vertex(pose, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this
    }

    open fun color(packed: IntArray, offset: Int): NeoVertexConsumer {
        color(packed[offset])
        return this
    }

    open fun color(r: Float, g: Float, b: Float, a: Float): NeoVertexConsumer {
        color((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), (a * 255).toInt())
        return this
    }

    open fun color(color: NeoColor): NeoVertexConsumer {
        color(color.red, color.green, color.blue, color.alpha ?: 255)
        return this
    }

    open fun color(argb: Int): NeoVertexConsumer {
        color(argb ushr 16 and 0xFF, argb ushr 8 and 0xFF, argb and 0xFF, argb ushr 24)
        return this
    }

    open fun textureUV(packed: IntArray, offset: Int): NeoVertexConsumer {
        textureUV(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1])
        )
        return this
    }

    open fun textureUV(uv: AbstractVec2<Float>): NeoVertexConsumer {
        textureUV(uv.x, uv.y)
        return this
    }

    open fun overlayUV(packed: IntArray, offset: Int): NeoVertexConsumer {
        overlayUV(packed[offset])
        return this
    }

    open fun overlayUV(uv: AbstractVec2<Int>): NeoVertexConsumer {
        overlayUV(uv.x, uv.y)
        return this
    }

    open fun overlayUV(packed: Int): NeoVertexConsumer {
        overlayUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this
    }

    open fun lightUV(packed: IntArray, offset: Int): NeoVertexConsumer {
        lightUV(packed[offset])
        return this
    }

    open fun lightUV(uv: AbstractVec2<Int>): NeoVertexConsumer {
        lightUV(uv.x, uv.y)
        return this
    }

    open fun lightUV(packed: Int): NeoVertexConsumer {
        lightUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this
    }

    open fun normal(packed: IntArray, offset: Int): NeoVertexConsumer {
        normal(packed[offset])
        return this
    }

    open fun normal(normal: AbstractVec3<*>): NeoVertexConsumer {
        normal(normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
        return this
    }

    open fun normal(packed: Int): NeoVertexConsumer {
        normal(
            (packed ushr 24) / 127f,
            ((packed ushr 16) and 0xFF) / 127f,
            ((packed ushr 8) and 0xFF) / 127f
        )
        return this
    }

    open fun normal(pose: PoseStack.Pose, normal: AbstractVec3<*>): NeoVertexConsumer {
        normal(pose, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
        return this
    }

    open fun cube(
        box: AbstractRect3<*>,
    ): NeoVertexConsumer {
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

        return this
    }

    open fun quad(
        v0: AbstractVec3<*>,
        v1: AbstractVec3<*>,
        v2: AbstractVec3<*>,
        v3: AbstractVec3<*>,
        normal: AbstractVec3<*>
    ): NeoVertexConsumer {
        vertex(v0).textureUV(0f, 1f).normal(normal)
        vertex(v1).textureUV(1f, 1f).normal(normal)
        vertex(v2).textureUV(1f, 0f).normal(normal)
        vertex(v3).textureUV(0f, 0f).normal(normal)

        return this
    }

    open class Redirect(
        private val other: NeoVertexConsumer
    ) : NeoVertexConsumer() {
        override fun vertex(
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            other.vertex(x, y, z)
            return this
        }

        override fun vertex(
            packed: IntArray,
            offset: Int
        ): NeoVertexConsumer {
            other.vertex(packed, offset)
            return this
        }

        override fun vertex(vertex: AbstractVec3<*>): NeoVertexConsumer {
            other.vertex(vertex)
            return this
        }

        override fun vertex(
            mat: Matrix4fc,
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            other.vertex(mat, x, y, z)
            return this
        }

        override fun vertex(
            mat: Matrix4fc,
            vertex: AbstractVec3<*>
        ): NeoVertexConsumer {
            other.vertex(mat, vertex)
            return this
        }

        override fun vertex(
            pose: PoseStack.Pose,
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            other.vertex(pose, x, y, z)
            return this
        }

        override fun vertex(
            pose: PoseStack.Pose,
            vertex: AbstractVec3<*>
        ): NeoVertexConsumer {
            other.vertex(pose, vertex)
            return this
        }

        override fun color(
            r: Int,
            g: Int,
            b: Int,
            a: Int
        ): NeoVertexConsumer {
            other.color(r, g, b, a)
            return this
        }

        override fun color(
            packed: IntArray,
            offset: Int
        ): NeoVertexConsumer {
            other.color(packed, offset)
            return this
        }

        override fun color(
            r: Float,
            g: Float,
            b: Float,
            a: Float
        ): NeoVertexConsumer {
            other.color(r, g, b, a)
            return this
        }

        override fun color(color: NeoColor): NeoVertexConsumer {
            other.color(color)
            return this
        }

        override fun color(argb: Int): NeoVertexConsumer {
            other.color(argb)
            return this
        }

        override fun textureUV(
            u: Float,
            v: Float
        ): NeoVertexConsumer {
            other.textureUV(u, v)
            return this
        }

        override fun textureUV(
            packed: IntArray,
            offset: Int
        ): NeoVertexConsumer {
            other.textureUV(packed, offset)
            return this
        }

        override fun textureUV(uv: AbstractVec2<Float>): NeoVertexConsumer {
            other.textureUV(uv)
            return this
        }

        override fun overlayUV(
            u: Int,
            v: Int
        ): NeoVertexConsumer {
            other.overlayUV(u, v)
            return this
        }

        override fun overlayUV(
            packed: IntArray,
            offset: Int
        ): NeoVertexConsumer {
            other.overlayUV(packed, offset)
            return this
        }

        override fun overlayUV(uv: AbstractVec2<Int>): NeoVertexConsumer {
            other.overlayUV(uv)
            return this
        }

        override fun overlayUV(packed: Int): NeoVertexConsumer {
            other.overlayUV(packed)
            return this
        }

        override fun lightUV(
            u: Int,
            v: Int
        ): NeoVertexConsumer {
            other.lightUV(u, v)
            return this
        }

        override fun lightUV(
            packed: IntArray,
            offset: Int
        ): NeoVertexConsumer {
            other.lightUV(packed, offset)
            return this
        }

        override fun lightUV(uv: AbstractVec2<Int>): NeoVertexConsumer {
            other.lightUV(uv)
            return this
        }

        override fun lightUV(packed: Int): NeoVertexConsumer {
            other.lightUV(packed)
            return this
        }

        override fun normal(
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            other.normal(x, y, z)
            return this
        }

        override fun normal(
            pose: PoseStack.Pose,
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            other.normal(pose, x, y, z)
            return this
        }

        override fun normal(
            packed: IntArray,
            offset: Int
        ): NeoVertexConsumer {
            other.normal(packed, offset)
            return this
        }

        override fun normal(normal: AbstractVec3<*>): NeoVertexConsumer {
            other.normal(normal)
            return this
        }

        override fun normal(packed: Int): NeoVertexConsumer {
            other.normal(packed)
            return this
        }

        override fun normal(
            pose: PoseStack.Pose,
            normal: AbstractVec3<*>
        ): NeoVertexConsumer {
            other.normal(pose, normal)
            return this
        }

        override fun _endVertex() {
            other._endVertex()
        }

        override fun cube(box: AbstractRect3<*>): NeoVertexConsumer {
            other.cube(box)
            return this
        }

        override fun quad(
            v0: AbstractVec3<*>,
            v1: AbstractVec3<*>,
            v2: AbstractVec3<*>,
            v3: AbstractVec3<*>,
            normal: AbstractVec3<*>
        ): NeoVertexConsumer {
            other.quad(v0, v1, v2, v3, normal)
            return this
        }
    }
}