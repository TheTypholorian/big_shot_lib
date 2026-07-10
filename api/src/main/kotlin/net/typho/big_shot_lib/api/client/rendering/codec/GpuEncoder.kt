package net.typho.big_shot_lib.api.client.rendering.codec

interface GpuEncoder<P : GpuPacking, A> : DynamicGpuEncoder<P, A> {
    val alignment: Long
    val size: Long
        get() = alignment

    override fun sizeOf(value: A): Long {
        return size
    }

    override fun alignmentOf(value: A): Long {
        return alignment
    }
}