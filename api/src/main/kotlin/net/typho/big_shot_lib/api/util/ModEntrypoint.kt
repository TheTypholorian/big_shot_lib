package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

abstract class ModEntrypoint(
    modId: String
) {
    val container = PlatformUtil.INSTANCE.getMod(modId)!!

    fun id(path: String) = NeoIdentifier(container.id, path)
}