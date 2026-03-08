package net.typho.big_shot_lib.api.client.opengl.state.arguments

@JvmRecord
data class RenderArgumentValueProvider<T, P>(
    @JvmField
    val type: RenderArgumentType<T>,
    @JvmField
    val provider: (parent: P) -> T
)
