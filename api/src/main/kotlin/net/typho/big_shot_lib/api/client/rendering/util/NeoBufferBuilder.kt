package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlIndexDataType
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.system.NativeResource

abstract class NeoBufferBuilder : NeoVertexConsumer(), NativeResource {
    abstract val format: NeoVertexFormat
    abstract val mode: GlBeginMode

    abstract fun put(element: NeoVertexFormat.Element, data: NeoBuffer.(index: Long) -> Unit)

    protected abstract fun end()

    abstract fun build(): Built?

    override fun vertex(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        end()
        put(NeoVertexFormat.Element.POSITION) { index ->
            put(index, x)
            put(index + 4, y)
            put(index + 8, z)
        }
        return this
    }

    override fun color(
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.COLOR) { index ->
            put(index, r.toByte())
            put(index + 1, g.toByte())
            put(index + 2, b.toByte())
            put(index + 3, a.toByte())
        }
        return this
    }

    override fun textureUV(
        u: Float,
        v: Float
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.TEXTURE_UV) { index ->
            put(index, u)
            put(index + 4, v)
        }
        return this
    }

    override fun overlayUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.OVERLAY_UV) { index ->
            put(index, u.toShort())
            put(index + 2, v.toShort())
        }
        return this
    }

    override fun lightUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.LIGHT_UV) { index ->
            put(index, u.toShort())
            put(index + 2, v.toShort())
        }
        return this
    }

    override fun normal(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.NORMAL) { index ->
            put(index, (x * 127).toInt().toByte())
            put(index + 1, (y * 127).toInt().toByte())
            put(index + 2, (z * 127).toInt().toByte())
        }
        return this
    }

    override fun vertex(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        end()
        put(NeoVertexFormat.Element.POSITION) { index ->
            put(index, packed[offset])
            put(index + 4, packed[offset + 1])
            put(index + 8, packed[offset + 2])
        }
        return this
    }

    override fun color(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.COLOR) { index ->
            put(index, packed[offset])
        }
        return this
    }

    override fun color(argb: Int): NeoVertexConsumer {
        put(NeoVertexFormat.Element.COLOR) { index ->
            put(index, argb and 0xFFFFFF)
            put(index + 3, (argb ushr 24).toByte())
        }
        return this
    }

    override fun textureUV(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.TEXTURE_UV) { index ->
            put(index, packed[offset])
            put(index + 4, packed[offset + 1])
        }
        return this
    }

    override fun overlayUV(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.OVERLAY_UV) { index ->
            put(index, packed[offset])
        }
        return this
    }

    override fun overlayUV(packed: Int): NeoVertexConsumer {
        put(NeoVertexFormat.Element.OVERLAY_UV) { index ->
            put(index, packed)
        }
        return this
    }

    override fun lightUV(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.LIGHT_UV) { index ->
            put(index, packed[offset])
        }
        return this
    }

    override fun lightUV(packed: Int): NeoVertexConsumer {
        put(NeoVertexFormat.Element.LIGHT_UV) { index ->
            put(index, packed)
        }
        return this
    }

    override fun normal(
        packed: IntArray,
        offset: Int
    ): NeoVertexConsumer {
        put(NeoVertexFormat.Element.NORMAL) { index ->
            put(index, packed[offset])
        }
        return this
    }

    override fun normal(packed: Int): NeoVertexConsumer {
        put(NeoVertexFormat.Element.NORMAL) { index ->
            put(index, packed)
        }
        return this
    }

    interface Built {
        val vertexBuffer: NeoBuffer
        val indexBuffer: NeoBuffer?
        val format: NeoVertexFormat
        val vertexCount: Int
        val indexCount: Int?
        val mode: GlBeginMode
        val indexType: GlIndexDataType?
    }
}