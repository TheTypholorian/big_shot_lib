package net.typho.big_shot_lib.api.client.ext

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.InternalClientUtil
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuDataType
import net.typho.big_shot_lib.api.client.rendering.util.mesh.NeoVertexFormats
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.Extension.Companion.cast

interface VertexFormatBuilderExtension : Extension<VertexFormat.Builder> {
    fun position(): VertexFormat.Builder {
        return InternalClientUtil.addPositionElement(cast())
    }

    fun textureUV(): VertexFormat.Builder {
        return InternalClientUtil.addTextureUvElement(cast())
    }

    fun overlayUV(): VertexFormat.Builder {
        return InternalClientUtil.addOverlayUvElement(cast())
    }

    fun lightUV(): VertexFormat.Builder {
        return InternalClientUtil.addLightUvElement(cast())
    }

    fun color(): VertexFormat.Builder {
        return InternalClientUtil.addColorElement(cast())
    }

    fun normal(): VertexFormat.Builder {
        return InternalClientUtil.addNormalElement(cast())
    }

    fun attribute(
        name: String,
        type: GpuDataType,
        components: Int
    ): VertexFormat.Builder {
        return InternalClientUtil.addCustomElement(cast(), name, type, components, null)
    }

    fun attribute(
        name: String,
        type: GpuDataType,
        components: Int,
        stride: Int
    ): VertexFormat.Builder {
        return InternalClientUtil.addCustomElement(cast(), name, type, components, stride)
    }

    @Suppress("DEPRECATION")
    fun build(location: Identifier): VertexFormat {
        val format = cast().build()
        NeoVertexFormats.register(location, format)
        return format
    }
}