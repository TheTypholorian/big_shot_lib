package net.typho.big_shot_lib.gl

interface Bindable {
    fun bind(): Unbindable

    fun bind(stack: GlResourceStack) = stack.put(bind())
}