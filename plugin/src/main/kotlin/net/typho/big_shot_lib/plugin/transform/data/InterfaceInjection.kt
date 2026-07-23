package net.typho.big_shot_lib.plugin.transform.data

import java.io.Serializable

data class InterfaceInjection(
    @JvmField
    val iface: String,
    @JvmField
    val target: String,
    @JvmField
    val typeParams: List<String>
) : Serializable