package net.typho.big_shot_lib.gl.stack

import net.typho.big_shot_lib.buffers.IIndexedBuffer
import net.typho.big_shot_lib.gl.resource.GlResourceInstance
import net.typho.big_shot_lib.gl.state.GlFlag
import net.typho.big_shot_lib.gl.state.GlState

interface GlBinder {
    fun bind(resource: GlResourceInstance)

    fun bindBase(resource: IIndexedBuffer, index: Int)

    fun <T : GlState.Value, S : GlState<T>> set(value: T)

    fun <T, S : GlState<T>> set(state: S, value: T)

    fun enable(flag: GlFlag)

    fun disable(flag: GlFlag)

    fun restoreDefaultFlags()

    fun restoreDefaultStates()

    fun unbindAllResources()

    fun unbindAllBases()
}