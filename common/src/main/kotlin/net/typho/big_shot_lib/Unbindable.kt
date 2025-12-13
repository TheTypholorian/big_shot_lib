package net.typho.big_shot_lib

interface Unbindable {
    fun getResource(): GlResourceInstance?

    fun unbind() = getResource()!!.type()!!.unbind()
}