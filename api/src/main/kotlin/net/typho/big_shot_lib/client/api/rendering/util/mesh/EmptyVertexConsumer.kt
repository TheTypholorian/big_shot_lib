package net.typho.big_shot_lib.client.api.rendering.util.mesh

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.QuadInstance
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.renderer.block.model.BakedQuad
import net.typho.big_shot_lib.api.math.IRect3
import net.typho.big_shot_lib.api.math.IVec2
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.buffer.MemoryWriter
import org.joml.Matrix3x2fc
import org.joml.Matrix4fc
import org.joml.Vector3f
import org.joml.Vector3fc
import org.lwjgl.system.MemoryStack
import java.util.function.Consumer

object EmptyVertexConsumer : SimpleVertexConsumer {
    override fun addVertex(
        x: Float,
        y: Float,
        z: Float
    ): VertexConsumer {
        return this
    }

    override fun addVertex(
        x: Float,
        y: Float,
        z: Float,
        color: Int,
        u: Float,
        v: Float,
        overlayCoords: Int,
        lightCoords: Int,
        nx: Float,
        ny: Float,
        nz: Float
    ) {
    }

    override fun addVertex(position: Vector3fc): VertexConsumer {
        return this
    }

    override fun addVertex(
        pose: PoseStack.Pose,
        position: Vector3fc
    ): VertexConsumer {
        return this
    }

    override fun addVertex(
        pose: PoseStack.Pose,
        x: Float,
        y: Float,
        z: Float
    ): VertexConsumer {
        return this
    }

    override fun addVertex(
        pose: Matrix4fc,
        x: Float,
        y: Float,
        z: Float
    ): VertexConsumer {
        return this
    }

    override fun setColor(
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ): VertexConsumer {
        return this
    }

    override fun setColor(color: Int): VertexConsumer {
        return this
    }

    override fun setColor(
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ): VertexConsumer {
        return this
    }

    override fun setUv(u: Float, v: Float): VertexConsumer {
        return this
    }

    override fun setUv1(u: Int, v: Int): VertexConsumer {
        return this
    }

    override fun setUv2(u: Int, v: Int): VertexConsumer {
        return this
    }

    override fun setNormal(
        x: Float,
        y: Float,
        z: Float
    ): VertexConsumer {
        return this
    }

    override fun setNormal(
        pose: PoseStack.Pose,
        x: Float,
        y: Float,
        z: Float
    ): VertexConsumer {
        return this
    }

    override fun setNormal(
        pose: PoseStack.Pose,
        normal: Vector3fc
    ): VertexConsumer {
        return this
    }

    override fun setLight(packedLightCoords: Int): VertexConsumer {
        return this
    }

    override fun setOverlay(packedOverlayCoords: Int): VertexConsumer {
        return this
    }

    override fun putBlockBakedQuad(
        x: Float,
        y: Float,
        z: Float,
        quad: BakedQuad,
        instance: QuadInstance
    ) {
    }

    override fun putBakedQuad(
        pose: PoseStack.Pose,
        quad: BakedQuad,
        instance: QuadInstance
    ) {
    }

    override fun addVertexWith2DPose(
        pose: Matrix3x2fc,
        x: Float,
        y: Float
    ): VertexConsumer {
        return this
    }

    override fun addVertex(
        pos: IVec3<Float>,
        color: NeoColor?,
        textureUV: IVec2<Float>?,
        overlayUV: IVec2<Int>?,
        lightUV: IVec2<Int>?,
        normal: IVec3<Float>?
    ): VertexConsumer {
        return this
    }

    override fun addVertex(vertex: PrimitiveVertex): VertexConsumer {
        return this
    }

    override fun addVertex(
        pose: PoseStack.Pose,
        vertex: PrimitiveVertex
    ): VertexConsumer {
        return this
    }

    override fun addVertex(packed: IntArray, offset: Int): VertexConsumer {
        return this
    }

    override fun addVertex(vertex: IVec3<*>): VertexConsumer {
        return this
    }

    override fun addVertex(
        mat: Matrix4fc,
        vertex: IVec3<*>
    ): VertexConsumer {
        return this
    }

    override fun addVertex(
        mat: Matrix4fc,
        vertex: Vector3f
    ): VertexConsumer {
        return this
    }

    override fun addVertex(
        pose: PoseStack.Pose,
        vertex: IVec3<*>
    ): VertexConsumer {
        return this
    }

    override fun setColor(packed: IntArray, offset: Int): VertexConsumer {
        return this
    }

    override fun setColor(color: NeoColor): VertexConsumer {
        return this
    }

    override fun setUv(packed: IntArray, offset: Int): VertexConsumer {
        return this
    }

    override fun setUv(uv: IVec2<Float>): VertexConsumer {
        return this
    }

    override fun setUv1(packed: IntArray, offset: Int): VertexConsumer {
        return this
    }

    override fun setUv1(uv: IVec2<Int>): VertexConsumer {
        return this
    }

    override fun setUv2(packed: IntArray, offset: Int): VertexConsumer {
        return this
    }

    override fun setUv2(uv: IVec2<Int>): VertexConsumer {
        return this
    }

    override fun setNormal(packed: IntArray, offset: Int): VertexConsumer {
        return this
    }

    override fun setNormal(normal: IVec3<*>): VertexConsumer {
        return this
    }

    override fun setNormal(x: Byte, y: Byte, z: Byte): VertexConsumer {
        return this
    }

    override fun setNormal(packed: Int): VertexConsumer {
        return this
    }

    override fun setNormal(
        pose: PoseStack.Pose,
        normal: IVec3<*>
    ): VertexConsumer {
        return this
    }

    override fun setNormal(
        pose: PoseStack.Pose,
        packed: Int
    ): VertexConsumer {
        return this
    }

    override fun custom(
        element: VertexFormatElement,
        out: Consumer<MemoryWriter>
    ): VertexConsumer {
        return this
    }

    override fun cube(box: IRect3<*>): VertexConsumer {
        return this
    }

    override fun quad(
        v0: IVec3<*>,
        v1: IVec3<*>,
        v2: IVec3<*>,
        v3: IVec3<*>,
        normal: IVec3<*>
    ): VertexConsumer {
        return this
    }

    override fun push(
        stack: MemoryStack,
        ptr: Long,
        count: Int,
        format: VertexFormat
    ) {
    }
}