package net.typho.big_shot_lib.api

import org.lwjgl.system.NativeResource

abstract class GlResource(
    @JvmField
    val glId: Int
) : GlNamed, Bindable, NativeResource {
    protected abstract fun bind(glId: Int)

    final override fun glId() = glId

    override fun bind() = bind(glId)

    override fun unbind() = bind(0)

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GlResource

        return glId == other.glId
    }

    override fun hashCode(): Int {
        return glId
    }

    abstract class Indexed(glId: Int) : GlResource(glId), IndexedBindable {
        protected abstract fun bindBase(index: Int, glId: Int)

        override fun bindBase(index: Int) = bindBase(index, glId)

        override fun unbindBase(index: Int) = bindBase(index, 0)
    }
}