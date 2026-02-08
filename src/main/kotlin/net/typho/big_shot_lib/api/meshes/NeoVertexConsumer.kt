package net.typho.big_shot_lib.api.meshes

import com.mojang.blaze3d.vertex.PoseStack
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
}