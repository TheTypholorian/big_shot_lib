package net.typho.big_shot_lib.meshes

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import org.joml.*
import java.awt.Color

interface IVertexConsumer {
    fun vertex(x: Float, y: Float, z: Float): IVertexConsumer

    fun color(r: Float, g: Float, b: Float, a: Float): IVertexConsumer

    fun textureUV(u: Float, v: Float): IVertexConsumer

    fun overlayUV(u: Int, v: Int): IVertexConsumer

    fun lightUV(u: Int, v: Int): IVertexConsumer

    fun normal(x: Float, y: Float, z: Float): IVertexConsumer

    fun vertex(vertex: Vector3fc): IVertexConsumer {
        vertex(vertex.x(), vertex.y(), vertex.z())
        return this
    }

    fun vertex(mat: Matrix4f, x: Float, y: Float, z: Float): IVertexConsumer {
        vertex(mat.transformPosition(x, y, z, Vector3f()))
        return this
    }

    fun vertex(mat: Matrix4f, vertex: Vector3fc): IVertexConsumer {
        vertex(mat, vertex.x(), vertex.y(), vertex.z())
        return this
    }

    fun vertex(pose: PoseStack.Pose, x: Float, y: Float, z: Float): IVertexConsumer {
        vertex(pose.pose(), x, y, z)
        return this
    }

    fun vertex(pose: PoseStack.Pose, vertex: Vector3fc): IVertexConsumer {
        vertex(pose, vertex.x(), vertex.y(), vertex.z())
        return this
    }

    fun color(r: Int, g: Int, b: Int, a: Int): IVertexConsumer {
        color(r / 255f, g / 255f, b / 255f, a / 255f)
        return this
    }

    fun color(color: Vector4fc): IVertexConsumer {
        color(color.x(), color.y(), color.z(), color.w())
        return this
    }

    fun color(color: Color): IVertexConsumer {
        color(color.red, color.green, color.blue, color.alpha)
        return this
    }

    fun textureUV(uv: Vector2fc): IVertexConsumer {
        textureUV(uv.x(), uv.y())
        return this
    }

    fun overlayUV(packed: Int): IVertexConsumer {
        overlayUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this
    }

    fun lightUV(packed: Int): IVertexConsumer {
        lightUV(packed and 0xFFFF, (packed shr 16) and 0xFFFF)
        return this
    }

    fun normal(normal: Vector3fc): IVertexConsumer {
        normal(normal.x(), normal.y(), normal.z())
        return this
    }

    fun normal(pose: PoseStack.Pose, x: Float, y: Float, z: Float): IVertexConsumer {
        normal(pose.transformNormal(x, y, z, Vector3f()))
        return this
    }

    fun normal(pose: PoseStack.Pose, normal: Vector3fc): IVertexConsumer {
        normal(pose, normal.x(), normal.y(), normal.z())
        return this
    }

    fun toBuiltin(): VertexConsumer = NeoVertexConsumer(this)

    companion object {
        fun wrap(builtin: VertexConsumer): IVertexConsumer = BuiltinVertexConsumer(builtin)
    }
}