package net.typho.big_shot_lib.api.render_queue

import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.Named

interface RenderSettingShard : Bindable {
    fun type(): Type<*>

    interface Type<S : RenderSettingShard> : Named {
        fun getDefault(): S

        fun codec(): MapCodec<S>
    }

    open class Basic(
        @JvmField
        val type: Type<*>,
        @JvmField
        val list: List<Bindable>
    ) : RenderSettingShard {
        override fun bind() {
            list.forEach { it.bind() }
        }

        override fun unbind() {
            list.forEach { it.unbind() }
        }

        override fun type() = type
    }
}