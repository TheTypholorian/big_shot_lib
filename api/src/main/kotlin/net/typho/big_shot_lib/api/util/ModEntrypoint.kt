package net.typho.big_shot_lib.api.util

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.platform.PlatformUtil

abstract class ModEntrypoint(
    modId: String
) {
    val container = PlatformUtil.INSTANCE.getMod(modId)!!

    fun id(path: String): Identifier = Identifier.of(container.id, path)
}