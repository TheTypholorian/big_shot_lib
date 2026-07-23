package net.typho.big_shot_lib.plugin.transform.data

import java.io.Serializable

data class StaticMethodInjection(
    @JvmField
    val redirectTo: MethodDesc,
    @JvmField
    val targetClass: String,
    @JvmField
    val targetMethodName: String,
    @JvmField
    val signature: String?,
    @JvmField
    val exceptions: List<String>
) : Serializable