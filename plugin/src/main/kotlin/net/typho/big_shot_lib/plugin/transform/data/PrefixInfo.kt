package net.typho.big_shot_lib.plugin.transform.data

import java.io.Serializable

data class PrefixInfo(
    @JvmField
    val prefixString: String,
    @JvmField
    val ignoreSubclasses: List<String>
) : Serializable