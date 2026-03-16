package net.typho.big_shot_lib.api.client.rendering.opengl

import kotlin.enums.enumEntries

interface GlNamed {
    val glId: Int

    companion object {
        @JvmStatic
        inline fun <reified E> getEnum(glId: Int): E where E : Enum<E>, E : GlNamed {
            return enumEntries<E>().first { it.glId == glId }
        }
    }
}