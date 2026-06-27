package net.typho.big_shot_lib.api.ext

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.Extension.Companion.cast

interface IdentifierExtension : Extension<Identifier> {
    fun toShortString(): String {
        val id = cast()
        return if (id.namespace == Identifier.DEFAULT_NAMESPACE) id.path else id.toString()
    }

    fun toString(delimiter: Char): String {
        val id = cast()
        return "${id.namespace}${delimiter}${id.path}"
    }

    fun toShortString(delimiter: Char): String {
        val id = cast()
        return if (id.namespace == Identifier.DEFAULT_NAMESPACE) id.path else toString(delimiter)
    }
}