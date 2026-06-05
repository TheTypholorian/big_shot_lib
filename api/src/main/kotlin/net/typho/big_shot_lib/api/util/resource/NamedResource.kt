package net.typho.big_shot_lib.api.util.resource

import net.minecraft.resources.Identifier

interface NamedResource : MaybeNamedResource {
    override val location: Identifier
}