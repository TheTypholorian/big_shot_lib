package net.typho.big_shot_lib.api.util.resource

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi

interface NamedResource : MaybeNamedResource {
    override val location: Identifier
}