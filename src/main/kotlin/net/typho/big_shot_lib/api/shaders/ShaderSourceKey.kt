package net.typho.big_shot_lib.api.shaders

@JvmRecord
data class ShaderSourceKey(
    @JvmField
    val program: ShaderProgramKey,
    @JvmField
    val type: ShaderSourceType
) {
    fun fileName(): String = "${program.location}.${type.extension}"
}
