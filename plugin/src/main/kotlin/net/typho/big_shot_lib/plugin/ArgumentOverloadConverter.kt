package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.data.MethodDesc
import java.io.Serializable

data class ArgumentOverloadConverter(
    @JvmField
    val from: String,
    @JvmField
    val to: String,
    @JvmField
    val converter: MethodDesc,
    @JvmField
    val permutate: Boolean
) : Serializable