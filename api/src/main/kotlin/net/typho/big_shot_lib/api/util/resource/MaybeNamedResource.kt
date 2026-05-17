package net.typho.big_shot_lib.api.util.resource

import net.minecraft.resources.Identifier

interface MaybeNamedResource {
    val location: Identifier?
}