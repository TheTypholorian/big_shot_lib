package net.typho.big_shot_lib

interface GlResourceInstance {
    fun type(): GlResourceType?

    fun id(): Int
}