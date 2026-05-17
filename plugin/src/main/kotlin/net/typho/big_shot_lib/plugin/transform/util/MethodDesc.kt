package net.typho.big_shot_lib.plugin.transform.util

data class MethodDesc(
    @JvmField
    val cls: ClassDesc,
    @JvmField
    val name: String,
    @JvmField
    val desc: String
)
