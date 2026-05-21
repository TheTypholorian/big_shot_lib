package net.typho.big_shot_lib.api.ext

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.plugin.Namespace

@Namespace(BigShotApi.MOD_ID)
interface IdentifierExtension {
    fun toShortString(): String
}