package net.typho.big_shot_lib.api.client.rendering.vulkan

import kotlin.enums.enumEntries

interface VkConst {
    val vkId: Int

    companion object {
        @JvmStatic
        inline fun <reified E> getEnum(vkId: Int): E where E : Enum<E>, E : VkConst {
            return enumEntries<E>().first { it.vkId == vkId }
        }

        @JvmStatic
        fun <E> getEnum(cls: Class<E>, vkId: Int): E where E : Enum<E>, E : VkConst {
            return cls.enumConstants.first { it.vkId == vkId }
        }
    }
}