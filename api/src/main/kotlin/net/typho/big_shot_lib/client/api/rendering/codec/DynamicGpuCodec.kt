package net.typho.big_shot_lib.client.api.rendering.codec

interface DynamicGpuCodec<P : GpuPacking, A> : DynamicGpuEncoder<P, A>, GpuDecoder<P, A> {
    val packing: P?
}