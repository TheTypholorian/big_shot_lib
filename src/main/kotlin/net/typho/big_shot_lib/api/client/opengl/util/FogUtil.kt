package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.BigShotApi.loadService

interface FogUtil {
    fun upload(shader: GlShader)

    companion object {
        @JvmField
        val INSTANCE: FogUtil = FogUtil::class.loadService()
    }
}