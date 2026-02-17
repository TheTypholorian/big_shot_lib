package net.typho.big_shot_lib.api.util.resources

@JvmRecord
data class NeoTagKey<T>(
    @JvmField
    val registry: ResourceIdentifier,
    @JvmField
    val location: ResourceIdentifier
)
