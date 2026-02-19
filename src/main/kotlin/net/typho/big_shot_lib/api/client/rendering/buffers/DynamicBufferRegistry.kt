package net.typho.big_shot_lib.api.client.rendering.buffers

object DynamicBufferRegistry {
    @JvmField
    val buffers = HashMap<Int, DynamicBuffer>()

    init {
        register(NormalsDynamicBuffer)
        register(AlbedoDynamicBuffer)
    }

    @JvmStatic
    fun register(buffer: DynamicBuffer) {
        val location = buffers.size + 1
        buffers[location] = buffer
        buffer.setShaderLocation(location)
    }

    fun init() = Unit
}