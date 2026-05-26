package net.typho.big_shot_lib.api.util.resource

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.plugin.Namespace

@Namespace(BigShotApi.MOD_ID)
interface NamedResource : MaybeNamedResource {
    override val location: Identifier
}