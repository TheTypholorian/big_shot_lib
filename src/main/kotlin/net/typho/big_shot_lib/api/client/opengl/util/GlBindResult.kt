package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArgumentType

sealed interface GlBindResult {
    val success: Boolean
    val message: String

    fun expand(): List<GlBindResult>

    object Success : GlBindResult {
        override val success = true
        override val message = "Success"

        override fun expand() = listOf(this)

        override fun toString() = message
    }

    data class Error(
        override val message: String
    ) : GlBindResult {
        override val success = false

        override fun expand() = listOf(this)

        override fun toString() = message
    }

    data class MissingArguments(
        @JvmField
        val arguments: Collection<RenderArgumentType<*>>
    ) : GlBindResult {
        override val success = false
        override val message = "Missing arguments $arguments"

        constructor(
            vararg arguments: RenderArgumentType<*>
        ) : this(listOf(*arguments))

        override fun expand() = listOf(this)

        override fun toString() = message
    }

    data class Multiple(
        @JvmField
        val results: Collection<GlBindResult>
    ) : GlBindResult {
        override val success = results.all { it.success }
        override val message = if (success)
            Success.toString()
        else
            results.filter { !it.success }.joinToString(separator = "\n", prefix = "\n", transform = { it.message })

        constructor(
            vararg arguments: GlBindResult
        ) : this(listOf(*arguments))

        override fun expand() = results.flatMap { it.expand() }

        override fun toString() = if (success) message else "Error(s):$message"
    }
}