package net.typho.big_shot_lib.gl

interface Bindable {
    fun bind(): Unbindable<*>

    fun bind(stack: GlStack) = stack.put(bind())
}