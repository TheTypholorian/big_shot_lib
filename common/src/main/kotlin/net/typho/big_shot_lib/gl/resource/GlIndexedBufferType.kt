package net.typho.big_shot_lib.gl.resource

import org.lwjgl.opengl.GL30
import java.util.function.BiConsumer
import java.util.function.Consumer

open class GlIndexedBufferType : GlResourceType {
    constructor(
        glName: Int,
        namespace: Int,
        bind: Consumer<Int>,
        unbind: Runnable
    ) : super(glName, namespace, bind, unbind)

    constructor(
        name: Int,
        namespace: Int,
        bind: BiConsumer<Int, Int>
    ) : super(name, namespace, bind)

    constructor(
        name: Int,
        namespace: Int,
        bind: Consumer<Int>
    ) : super(name, namespace, bind)

    open fun bindBase(id: Int, index: Int) = GL30.glBindBufferBase(glName, index, id)

    open fun unbindBase(index: Int) = GL30.glBindBufferBase(glName, index, 0)
}