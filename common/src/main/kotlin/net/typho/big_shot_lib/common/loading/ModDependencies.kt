package net.typho.big_shot_lib.common.loading

data class ModDependencies(
    @JvmField
    val required: Map<String, String>,
    @JvmField
    val incompatible: Map<String, String>,
    @JvmField
    val problematic: Map<String, String>,
) {
    object Codec {
    }
}