package net.typho.big_shot_lib.api.client.rendering.state

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.shaders.GlShader

interface FogUtil {
    fun upload(shader: GlShader)

    companion object {
        @JvmField
        val INSTANCE: FogUtil = FogUtil::class.loadService()
    }
}
