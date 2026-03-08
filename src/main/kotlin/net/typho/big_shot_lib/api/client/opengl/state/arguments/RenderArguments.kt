package net.typho.big_shot_lib.api.client.opengl.state.arguments

import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@JvmRecord
data class RenderArguments(
    @JvmField
    val values: MutableSet<RenderArgumentValue<*>> = HashSet()
) {
    companion object {
        @JvmStatic
        val NONE = RenderArguments()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(type: RenderArgumentType<T>): T? = values.firstOrNull { it.type === type }?.value as? T ?: (type.default())

    @OptIn(ExperimentalContracts::class)
    inline fun <T> get(type: RenderArgumentType<T>, result: (result: GlBindResult) -> Nothing): T {
        contract {
            callsInPlace(result, InvocationKind.AT_MOST_ONCE)
        }

        val value = get(type)

        if (value == null) {
            result(GlBindResult.MissingArguments(type))
        } else {
            return value
        }
    }

    fun <T> set(type: RenderArgumentType<T>, value: T) {
        values.add(RenderArgumentValue(type, value))
    }

    fun hasAll(arguments: Collection<RenderArgumentType<*>>): GlBindResult {
        val arguments = HashSet(arguments)
        values.forEach { arguments.remove(it.type) }
        return if (arguments.isEmpty()) GlBindResult.Success else GlBindResult.MissingArguments(arguments)
    }

    fun push(arguments: Collection<RenderArgumentValue<*>>): RenderArguments {
        val values = HashSet(values)
        values.addAll(arguments)
        return RenderArguments(values)
    }
}