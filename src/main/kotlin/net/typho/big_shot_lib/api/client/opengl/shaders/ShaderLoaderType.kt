package net.typho.big_shot_lib.api.client.opengl.shaders

enum class ShaderLoaderType(
    @JvmField
    val autoBindLocations: Boolean
) {
    MINECRAFT(true),
    SODIUM(true),
    VEIL(true),
    IRIS(true),
    BIG_SHOT(true),
    NULL(false),
    OTHER(false)
}