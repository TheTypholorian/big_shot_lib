package net.typho.big_shot_lib.plugin.transform.data

import java.io.Serializable

data class FieldRename(
    @JvmField
    val classes: Set<String>?,
    @JvmField
    val descriptors: Set<String>?,
    @JvmField
    val from: String,
    @JvmField
    val to: String
) : Serializable