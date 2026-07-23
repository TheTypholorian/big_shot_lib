package net.typho.big_shot_lib.plugin.transform.data

import java.io.Serializable

data class FieldRename(
    @JvmField
    val from: FieldDesc,
    @JvmField
    val to: String
) : Serializable