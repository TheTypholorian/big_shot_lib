package net.typho.big_shot_lib.api.client.opengl.util

interface GlIndexedBindable {
    fun bindBase(index: Int)

    fun unbindBase(index: Int)
}