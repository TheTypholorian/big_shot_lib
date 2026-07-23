package net.typho.big_shot_lib.plugin.transform.data

import java.io.Serializable

data class MethodDesc(
    @JvmField
    val cls: String,
    @JvmField
    val name: String,
    @JvmField
    val desc: String
) : Serializable
