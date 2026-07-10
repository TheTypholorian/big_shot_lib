package net.typho.big_shot_lib.api.client.rendering.codec

interface DynamicGpuCodec<P : GpuPacking, A> : DynamicGpuEncoder<P, A>, GpuDecoder<P, A> {
    val packing: P?
}