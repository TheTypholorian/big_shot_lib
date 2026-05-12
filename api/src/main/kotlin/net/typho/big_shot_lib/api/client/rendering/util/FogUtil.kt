package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

interface FogUtil {
    fun upload(shader: GlBoundProgram)

    companion object {
        val INSTANCE by lazy { FogUtil::class.loadService() }
    }
}