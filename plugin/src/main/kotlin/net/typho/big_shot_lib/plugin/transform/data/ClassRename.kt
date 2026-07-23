package net.typho.big_shot_lib.plugin.transform.data

import java.io.Serializable

data class ClassRename(
    @JvmField
    val from: String,
    @JvmField
    val to: String
) : Serializable