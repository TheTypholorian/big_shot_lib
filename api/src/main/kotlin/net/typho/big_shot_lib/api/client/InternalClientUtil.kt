package net.typho.big_shot_lib.api.client

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuDataType
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

private val INSTANCE by lazy { IInternalClientUtil::class.loadService() }

object InternalClientUtil : IInternalClientUtil by INSTANCE

interface IInternalClientUtil {
    fun addPositionElement(builder: VertexFormat.Builder): VertexFormat.Builder

    fun addTextureUvElement(builder: VertexFormat.Builder): VertexFormat.Builder

    fun addOverlayUvElement(builder: VertexFormat.Builder): VertexFormat.Builder

    fun addLightUvElement(builder: VertexFormat.Builder): VertexFormat.Builder

    fun addColorElement(builder: VertexFormat.Builder): VertexFormat.Builder

    fun addNormalElement(builder: VertexFormat.Builder): VertexFormat.Builder

    fun addCustomElement(builder: VertexFormat.Builder, name: String, type: GpuDataType, components: Int, stride: Int?): VertexFormat.Builder
}