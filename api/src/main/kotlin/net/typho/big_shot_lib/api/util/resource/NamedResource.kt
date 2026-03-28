package net.typho.big_shot_lib.api.util.resource

interface NamedResource : MaybeNamedResource {
    override val location: NeoIdentifier
}