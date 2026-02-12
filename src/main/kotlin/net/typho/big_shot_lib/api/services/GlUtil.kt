package net.typho.big_shot_lib.api.services

import com.mojang.blaze3d.vertex.VertexFormat
import java.util.*

interface GlUtil {
    fun toGlId(mode: VertexFormat.Mode): Int

    fun toGlId(type: VertexFormat.IndexType): Int

    fun initBufferState(format: VertexFormat)

    companion object {
        @JvmField
        val INSTANCE: GlUtil = ServiceLoader.load(GlUtil::class.java).findFirst().orElseThrow()
    }
}