package net.typho.big_shot_lib.gl

interface Unbindable {
    fun getResource(): GlResourceInstance?

    fun unbind() = getResource()!!.type()!!.unbind()
}