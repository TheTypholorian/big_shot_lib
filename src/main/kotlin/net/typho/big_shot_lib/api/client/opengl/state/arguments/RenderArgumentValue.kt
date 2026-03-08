package net.typho.big_shot_lib.api.client.opengl.state.arguments

@JvmRecord
data class RenderArgumentValue<T>(
    @JvmField
    val type: RenderArgumentType<T>,
    @JvmField
    val value: T
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RenderArgumentValue<*>

        return type == other.type
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }
}
