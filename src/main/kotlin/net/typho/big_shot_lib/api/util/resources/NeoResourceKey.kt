package net.typho.big_shot_lib.api.util.resources

@JvmRecord
data class NeoResourceKey<T>(
    @JvmField
    val registry: ResourceIdentifier,
    @JvmField
    val location: ResourceIdentifier
)
