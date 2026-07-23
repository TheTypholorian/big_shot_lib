package net.typho.big_shot_lib.plugin.transform.data

import java.io.Serializable

data class MethodRename(
    @JvmField
    val from: MethodDesc,
    @JvmField
    val to: String
) : Serializable