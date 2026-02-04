package net.typho.big_shot_lib.api.render_queue.shards

import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.GlManager

sealed interface BlendFunction : Bindable {
    @JvmRecord
    data class Basic(
        @JvmField
        val src: BlendFactor,
        @JvmField
        val dst: BlendFactor
    ) : BlendFunction {
        override fun bind() {
            GlManager.blendFunc(src, dst)
        }

        override fun unbind() {
        }
    }

    @JvmRecord
    data class Separate(
        @JvmField
        val src: BlendFactor,
        @JvmField
        val dst: BlendFactor,
        @JvmField
        val srcA: BlendFactor,
        @JvmField
        val dstA: BlendFactor
    ) : BlendFunction {
        override fun bind() {
            GlManager.blendFuncSeparate(src, dst, srcA, dstA)
        }

        override fun unbind() {
        }
    }
}