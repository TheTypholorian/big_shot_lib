package net.typho.big_shot_lib.api.client.rendering.util

import org.lwjgl.system.NativeResource

fun interface BoundResource : NativeResource {
    fun unbind()

    override fun free() {
        unbind()
    }

    companion object {
        @JvmStatic
        fun all(vararg resources: BoundResource) = BoundResource {
            resources.forEach { it.unbind() }
        }

        @JvmStatic
        fun all(vararg resources: BoundResource?) = BoundResource {
            resources.forEach { it?.unbind() }
        }

        @JvmStatic
        fun all(vararg resources: () -> BoundResource): BoundResource {
            val bound = resources.map { it() }
            return BoundResource {
                bound.forEach { it.unbind() }
            }
        }

        @JvmStatic
        fun all(vararg resources: () -> BoundResource?): BoundResource {
            val bound = resources.map { it() }
            return BoundResource {
                bound.forEach { it?.unbind() }
            }
        }
    }
}