package net.typho.big_shot_lib.api.client.rendering

import com.mojang.blaze3d.vertex.PoseStack
import net.typho.big_shot_lib.api.math.rect.AbstractRect3
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.NeoColor
import org.joml.Matrix4f
import org.joml.Vector3f

interface NeoVertexConsumer {
    fun vertex(x: Float, y: Float, z: Float): NeoVertexConsumer

    fun color(r: Int, g: Int, b: Int, a: Int): NeoVertexConsumer

    fun textureUV(u: Float, v: Float): NeoVertexConsumer

    fun overlayUV(u: Int, v: Int): NeoVertexConsumer

    fun lightUV(u: Int, v: Int): NeoVertexConsumer

    fun normal(x: Float, y: Float, z: Float): NeoVertexConsumer

    fun vertex(packed: IntArray, offset: Int): NeoVertexConsumer {
        vertex(
            Float.fromBits(packed[offset]),
            Float.fromBits(packed[offset + 1]),
            Float.fromBits(packed[offset + 2])
        )
        return this
    }

    fun vertex(vertex: AbstractVec3<*, *>): NeoVertexConsumer {
        vertex(vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this
    }

    fun vertex(mat: Matrix4f, x: Float, y: Float, z: Float): NeoVertexConsumer {
        val transformed = mat.transformPosition(x, y, z, Vector3f())
        vertex(transformed.x, transformed.y, transformed.z)
        return this
    }

    fun vertex(mat: Matrix4f, vertex: AbstractVec3<*, *>): NeoVertexConsumer {
        vertex(mat, vertex.x.toFloat(), vertex.y.toFloat(), vertex.z.toFloat())
        return this
    }

    fun vertex(pose: PoseStack.Pose, x: Float, y: Float, z: Float): NeoVertexConsumer {
        vertex(pose.pose(), x, y, z)
        return this
    }

    fun vertex(pose: PoseStack.Pose, vertex: AbstractVec3<*, *>): NeoVertexConsumer {
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
        color(color.red, color.green, color.blue, color.alpha ?: 255)
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

    fun textureUV(uv: AbstractVec2<Float, *>): NeoVertexConsumer {
        textureUV(uv.x, uv.y)
        return this
    }

    fun overlayUV(packed: IntArray, offset: Int): NeoVertexConsumer {
        overlayUV(packed[offset])
        return this
    }

    fun overlayUV(uv: AbstractVec2<Int, *>): NeoVertexConsumer {
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

    fun lightUV(uv: AbstractVec2<Int, *>): NeoVertexConsumer {
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

    fun normal(normal: AbstractVec3<*, *>): NeoVertexConsumer {
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
        val transformed = pose.transformNormal(x, y, z, Vector3f())
        normal(transformed.x, transformed.y, transformed.z)
        return this
    }

    fun normal(pose: PoseStack.Pose, normal: AbstractVec3<*, *>): NeoVertexConsumer {
        normal(pose, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
        return this
    }

    /*
    fun toMojang(): VertexConsumer {
        return object : VertexConsumer {
            override fun addVertex(
                f: Float,
                g: Float,
                h: Float
            ): VertexConsumer {
                vertex(f, g, h)
                return this
            }

            override fun setColor(
                i: Int,
                j: Int,
                k: Int,
                l: Int
            ): VertexConsumer {
                color(i, j, k, l)
                return this
            }

            override fun setUv(f: Float, g: Float): VertexConsumer {
                textureUV(f, g)
                return this
            }

            override fun setUv1(i: Int, j: Int): VertexConsumer {
                overlayUV(i, j)
                return this
            }

            override fun setUv2(i: Int, j: Int): VertexConsumer {
                lightUV(i, j)
                return this
            }

            override fun setNormal(
                f: Float,
                g: Float,
                h: Float
            ): VertexConsumer {
                normal(f, g, h)
                return this
            }
        }
    }
     */

    fun cube(
        box: AbstractRect3<*, *, *>,
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

    fun quad(
        v0: AbstractVec3<*, *>,
        v1: AbstractVec3<*, *>,
        v2: AbstractVec3<*, *>,
        v3: AbstractVec3<*, *>,
        normal: AbstractVec3<*, *>
    ): NeoVertexConsumer {
        vertex(v0).textureUV(0f, 1f).normal(normal)
        vertex(v1).textureUV(1f, 1f).normal(normal)
        vertex(v2).textureUV(1f, 0f).normal(normal)
        vertex(v3).textureUV(0f, 0f).normal(normal)

        return this
    }
}