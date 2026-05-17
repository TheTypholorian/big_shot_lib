package net.typho.big_shot_lib.plugin.transform.util

data class FieldDesc(
    @JvmField
    val cls: ClassDesc,
    @JvmField
    val name: String,
    @JvmField
    val type: String
)
