package net.typho.big_shot_lib.api.ext

import net.minecraft.resources.Identifier

interface IdentifierExtension {
    fun toShortString(): String {
        val id = this as Identifier
        return if (id.namespace == Identifier.DEFAULT_NAMESPACE) id.path else id.toString()
    }

    fun toString(delimiter: Char): String {
        val id = this as Identifier
        return "${id.namespace}${delimiter}${id.path}"
    }

    fun toShortString(delimiter: Char): String {
        val id = this as Identifier
        return if (id.namespace == Identifier.DEFAULT_NAMESPACE) id.path else toString(delimiter)
    }
}