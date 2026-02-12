package net.typho.big_shot_lib.api.util

import com.mojang.blaze3d.vertex.VertexFormat
import java.util.*

interface GlConstUtil {
    fun toGlId(mode: VertexFormat.Mode): Int

    fun toGlId(type: VertexFormat.IndexType): Int

    companion object {
        @JvmField
        val INSTANCE: GlConstUtil = ServiceLoader.load(GlConstUtil::class.java).findFirst().orElseThrow()
    }
}