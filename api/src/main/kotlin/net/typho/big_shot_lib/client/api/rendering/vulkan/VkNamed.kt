package net.typho.big_shot_lib.client.api.rendering.vulkan

import kotlin.enums.enumEntries

interface VkNamed {
    val vkId: Long

    companion object {
        @JvmStatic
        inline fun <reified E> getEnum(vkId: Long): E where E : Enum<E>, E : VkNamed {
            return enumEntries<E>().first { it.vkId == vkId }
        }

        @JvmStatic
        fun <E> getEnum(cls: Class<E>, vkId: Long): E where E : Enum<E>, E : VkNamed {
            return cls.enumConstants.first { it.vkId == vkId }
        }
    }
}