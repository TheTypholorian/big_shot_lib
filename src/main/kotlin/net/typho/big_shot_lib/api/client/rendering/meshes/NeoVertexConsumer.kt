package net.typho.big_shot_lib.api.client.rendering.meshes

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.world.phys.AABB
import net.typho.big_shot_lib.api.util.IColor
import org.joml.*
import java.awt.Color

interface NeoVertexConsumer {
    fun vertex(x: Float, y: Float, z: Float): NeoVertexConsumer

    fun color(r: Float, g: Float, b: Float, a: Float): NeoVertexConsumer

    fun textureUV(u: Float, v: Float): NeoVertexConsumer

    fun overlayUV(u: Int, v: Int): NeoVertexConsumer

    fun lightUV(u: Int, v: Int): NeoVertexConsumer

    fun normal(x: Float, y: Float, z: Float): NeoVertexConsumer

    fun vertex(vertex: Vector3fc): NeoVertexConsumer {
        vertex(vertex.x(), vertex.y(), vertex.z())
        return this
    }

    fun vertex(mat: Matrix4f, x: Float, y: Float, z: Float): NeoVertexConsumer {
        vertex(mat.transformPosition(x, y, z, Vector3f()))
        return this
    }

    fun vertex(mat: Matrix4f, vertex: Vector3fc): NeoVertexConsumer {
        vertex(mat, vertex.x(), vertex.y(), vertex.z())
        return this
    }

    fun vertex(pose: PoseStack.Pose, x: Float, y: Float, z: Float): NeoVertexConsumer {
        vertex(pose.pose(), x, y, z)
        return this
    }

    fun vertex(pose: PoseStack.Pose, vertex: Vector3fc): NeoVertexConsumer {
        vertex(pose, vertex.x(), vertex.y(), vertex.z())
        return this
    }

    fun color(r: Int, g: Int, b: Int, a: Int): NeoVertexConsumer {
        color(r / 255f, g / 255f, b / 255f, a / 255f)
        return this
    }

    fun color(color: Vector4fc): NeoVertexConsumer {
        color(color.x(), color.y(), color.z(), color.w())
        return this
    }

    fun color(color: Color): NeoVertexConsumer {
        color(color.red, color.green, color.blue, color.alpha)
        return this
    }

    fun color(color: IColor): NeoVertexConsumer {
        color(color.toVec4F())
        return this
    }

    fun textureUV(uv: Vector2fc): NeoVertexConsumer {
        textureUV(uv.x(), uv.y())
        return this
    }

    fun overlayUV(packed: Int): NeoVertexConsumer {
        overlayUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this
    }

    fun lightUV(packed: Int): NeoVertexConsumer {
        lightUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this
    }

    fun normal(normal: Vector3fc): NeoVertexConsumer {
        normal(normal.x(), normal.y(), normal.z())
        return this
    }

    fun normal(pose: PoseStack.Pose, x: Float, y: Float, z: Float): NeoVertexConsumer {
        normal(pose.transformNormal(x, y, z, Vector3f()))
        return this
    }

    fun normal(pose: PoseStack.Pose, normal: Vector3fc): NeoVertexConsumer {
        normal(pose, normal.x(), normal.y(), normal.z())
        return this
    }

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

    fun cube(
        box: AABB,
    ): NeoVertexConsumer {
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

        return this
    }

    fun quad(
        v0: Vector3f,
        v1: Vector3f,
        v2: Vector3f,
        v3: Vector3f,
        normal: Vector3f
    ): NeoVertexConsumer {
        vertex(v0).textureUV(0f, 1f).normal(normal)
        vertex(v1).textureUV(1f, 1f).normal(normal)
        vertex(v2).textureUV(1f, 0f).normal(normal)
        vertex(v3).textureUV(0f, 0f).normal(normal)

        return this
    }
}