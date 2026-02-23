package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.client.util.dynamic_buffers.DynamicBuffer

interface DynamicBufferFactory {
    fun register(buffer: DynamicBuffer<*>): Int
}