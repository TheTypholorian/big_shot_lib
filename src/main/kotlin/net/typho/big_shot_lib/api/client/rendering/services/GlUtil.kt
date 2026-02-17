package net.typho.big_shot_lib.api.client.rendering.services

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.BigShotApi.loadService

interface GlUtil {
    fun toGlId(mode: VertexFormat.Mode): Int

    fun toGlId(type: VertexFormat.IndexType): Int

    fun initBufferState(format: VertexFormat)

    companion object {
        @JvmField
        val INSTANCE: GlUtil = GlUtil::class.loadService()
    }
}