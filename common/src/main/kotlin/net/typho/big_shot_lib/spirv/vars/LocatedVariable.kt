package net.typho.big_shot_lib.spirv.vars

data class LocatedVariable(
    val id: Int,
    val name: String?,
    val location: Int?,
    val typePointer: Int?,
    val type: Int?
)