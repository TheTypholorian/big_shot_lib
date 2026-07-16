package net.typho.big_shot_lib.client.api.rendering.codec

object Std140 : GpuPacking {
    override fun structBuilder() = StructBuilder()

    open class StructBuilder : GpuPacking.StructBuilder {
        @JvmField
        protected val components = mutableListOf<GpuCodec<*, *>>()
        @JvmField
        protected var last: DynamicGpuCodec<*, *>? = null

        override fun add(component: GpuCodec<*, *>): GpuPacking.StructBuilder {
            components.add(component)
            return this
        }

        override fun get(componentIndex: Int): Long {
            if (componentIndex == components.size) {
                last?.let { return it.numPaddingBytes }
            }

            val component = components[componentIndex]
            return component.alignment - component.size
        }

        override fun end(): Long {
            if (last != null) {
                throw IllegalStateException("Structs ending with a dynamic gpu codec do not have a set length")
            }

            return components.sumOf { it.alignment }
        }

        override fun <A> end(last: DynamicGpuCodec<*, A>): (A) -> Long {
            this.last = last
            val size = components.sumOf { it.alignment }
            return { size + last.alignmentOf(it) }
        }
    }
}