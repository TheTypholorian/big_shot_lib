package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.errors.GlBindException

class GlLayeredBind {
    private class Layer {
        private val bind: Collection<GlAdvancedBindable>
        private val unbind: Collection<GlAdvancedBindable>
        private val arguments: RenderArguments
        val result: GlBindResult

        constructor(
            bind: Collection<GlAdvancedBindable>,
            arguments: RenderArguments
        ) {
            val unbind = ArrayList<GlAdvancedBindable>()
            val results = ArrayList<GlBindResult>()
            this.bind = bind.filter { bindable ->
                val result = bindable.bind(arguments)

                if (result.success) {
                    unbind.add(bindable)
                    return@filter false
                } else if (result.expand().all { it is GlBindResult.MissingArguments }) {
                    results.add(result)
                    return@filter true
                } else {
                    throw GlBindException(result)
                }
            }
            this.unbind = unbind
            this.arguments = arguments
            this.result = if (results.isEmpty()) GlBindResult.Success else GlBindResult.Multiple(results)
        }

        constructor(
            last: Layer,
            args: (arguments: RenderArguments) -> Unit
        ) : this(last.bind, last.arguments.also(args))

        fun pop() {
            unbind.forEach { it.unbind() }
        }
    }

    private val layers = ArrayList<Layer>()

    constructor(
        bind: Collection<GlAdvancedBindable>,
        arguments: RenderArguments
    ) {
        layers.add(Layer(bind, arguments))
    }

    fun push(args: (arguments: RenderArguments) -> Unit) {
        layers.add(Layer(layers.last(), args))
    }

    fun pop() {
        layers.removeLast().pop()
    }

    fun checkErrors() {
        val result = layers.last().result

        if (!result.success) {
            throw GlBindException(result)
        }
    }
}