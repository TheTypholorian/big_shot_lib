package net.typho.big_shot_lib.api.client.opengl.shaders

@JvmRecord
data class ShaderSourceKey(
    @JvmField
    val program: ShaderProgramKey,
    @JvmField
    val type: ShaderSourceType
) {
    fun fileName(): String = "${program.location}.${type.extension}"

    override fun toString(): String {
        return "$program/${type.name.lowercase()}"
    }
}
