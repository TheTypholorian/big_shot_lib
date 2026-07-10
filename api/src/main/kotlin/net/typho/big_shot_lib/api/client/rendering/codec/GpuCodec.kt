package net.typho.big_shot_lib.api.client.rendering.codec

import net.typho.big_shot_lib.api.math.IVec2
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.math.IVec4
import net.typho.big_shot_lib.api.util.buffer.MemoryReader
import net.typho.big_shot_lib.api.util.buffer.MemoryWriter
import java.util.function.BiConsumer
import java.util.function.Function

@Suppress("UNUSED")
interface GpuCodec<P : GpuPacking, A> : DynamicGpuCodec<P, A>, GpuEncoder<P, A>, GpuDecoder<P, A> {
    override val numPaddingBytes: Long
        get() = alignment - size

    fun listOf() = object : DynamicGpuCodec<P, List<A>> {
        override val packing: P?
            get() = this@GpuCodec.packing
        override val numPaddingBytes: Long = 0

        override fun sizeOf(value: List<A>): Long {
            return value.size * size
        }

        override fun alignmentOf(value: List<A>): Long {
            return value.size * alignment
        }

        override fun encode(
            value: List<A>,
            output: MemoryWriter
        ) {
            for (element in value) {
                encode(element, output)
            }
        }

        override fun decode(input: MemoryReader): List<A> {
            val size = (input.bytesLeft() / size).toInt()
            val list = ArrayList<A>(size)
            repeat(size) { list.add(this@GpuCodec.decode(input)) }
            return list
        }
    }

    open class Impl<A>(
        @JvmField
        val encode: BiConsumer<MemoryWriter, A>,
        @JvmField
        val decode: Function<MemoryReader, A>,
        override val size: Long,
        override val alignment: Long = size,
        override val packing: GpuPacking? = null,
    ) : GpuCodec<GpuPacking, A> {
        override fun encode(value: A, output: MemoryWriter) {
            encode.accept(output, value)
        }

        override fun decode(input: MemoryReader): A {
            return decode.apply(input)
        }
    }

    companion object {
        @JvmField
        val BOOL: GpuCodec<GpuPacking, Boolean> = Impl({ output, v -> output.writeBoolean(v) }, { it.readBoolean() }, 4)
        @JvmField
        val INT: GpuCodec<GpuPacking, Int> = Impl({ output, v -> output.writeInt(v) }, { it.readInt() }, 4)
        @JvmField
        val FLOAT: GpuCodec<GpuPacking, Float> = Impl({ output, v -> output.writeFloat(v) }, { it.readFloat() }, 4)
        @JvmField
        val VEC2I: GpuCodec<GpuPacking, IVec2<Int>> = Impl(
            { output, v ->
                output.writeInt(v.x)
                output.writeInt(v.y)
            }, {
                IVec2(it.readInt(), it.readInt())
            },
            16
        )
        @JvmField
        val VEC2F: GpuCodec<GpuPacking, IVec2<Float>> = Impl(
            { output, v ->
                output.writeFloat(v.x)
                output.writeFloat(v.y)
            }, {
                IVec2(it.readFloat(), it.readFloat())
            },
            16
        )
        @JvmField
        val VEC3I: GpuCodec<GpuPacking, IVec3<Int>> = Impl(
            { output, v ->
                output.writeInt(v.x)
                output.writeInt(v.y)
                output.writeInt(v.z)
            }, {
                IVec3(it.readInt(), it.readInt(), it.readInt())
            },
            24,
            32
        )
        @JvmField
        val VEC3F: GpuCodec<GpuPacking, IVec3<Float>> = Impl(
            { output, v ->
                output.writeFloat(v.x)
                output.writeFloat(v.y)
                output.writeFloat(v.z)
            }, {
                IVec3(it.readFloat(), it.readFloat(), it.readFloat())
            },
            24,
            32
        )
        @JvmField
        val VEC4I: GpuCodec<GpuPacking, IVec4<Int>> = Impl(
            { output, v ->
                output.writeInt(v.x)
                output.writeInt(v.y)
                output.writeInt(v.z)
                output.writeInt(v.w)
            }, {
                IVec4(it.readInt(), it.readInt(), it.readInt(), it.readInt())
            },
            32
        )
        @JvmField
        val VEC4F: GpuCodec<GpuPacking, IVec4<Float>> = Impl(
            { output, v ->
                output.writeFloat(v.x)
                output.writeFloat(v.y)
                output.writeFloat(v.z)
            }, {
                IVec4(it.readFloat(), it.readFloat(), it.readFloat(), it.readFloat())
            },
            32
        )

        /*
        data class TestObject(
            val v0: Float,
            val v1: Float,
            val v2: Boolean,
            val v3: Int,
            val v4: IVec3<Int>,
            val v5: Boolean,
            val v6: IVec2<Int>,
            val v7: IVec2<Float>
        )

        @JvmField
        val TEST_CODEC = struct(
            FLOAT, TestObject::v0,
            FLOAT, TestObject::v1,
            BOOL, TestObject::v2,
            INT, TestObject::v3,
            VEC3I, TestObject::v4,
            BOOL, TestObject::v5,
            VEC2I, TestObject::v6,
            VEC2F, TestObject::v7,
            ::TestObject,
            Std140
        )

        @JvmStatic
        fun main(args: Array<String>) {
            val testObject = TestObject(
                1f, // good
                2f, // good
                true, // good
                -530252, // good
                IVec3(34, 6435, 547581), // good
                false, // good
                IVec2(3, 4),
                IVec2(5f, 6f)
            )
            TEST_CODEC.encode(testObject).use {
                it.read().run {
                    while (bytesLeft() > 0) {
                        println(readByte().toHexString())
                    }
                }
            }
        }
         */

        @JvmStatic
        fun <V0, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            constructor: (V0) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                alignment = builder.end()
                skip0 = builder[0]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            constructor: (V0, V1) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            constructor: (V0, V1, V2) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            constructor: (V0, V1, V2, V3) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, V4, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            codec4: GpuCodec<in P, V4>, getter4: (A) -> V4,
            constructor: (V0, V1, V2, V3, V4) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long
            val skip4: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                    .add(codec4)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
                skip4 = builder[4]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
                packing.encode(codec4, getter4(value), skip4, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input),
                    packing.decode(codec4, skip4, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, V4, V5, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            codec4: GpuCodec<in P, V4>, getter4: (A) -> V4,
            codec5: GpuCodec<in P, V5>, getter5: (A) -> V5,
            constructor: (V0, V1, V2, V3, V4, V5) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long
            val skip4: Long
            val skip5: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                    .add(codec4)
                    .add(codec5)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
                skip4 = builder[4]
                skip5 = builder[5]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
                packing.encode(codec4, getter4(value), skip4, output)
                packing.encode(codec5, getter5(value), skip5, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input),
                    packing.decode(codec4, skip4, input),
                    packing.decode(codec5, skip5, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, V4, V5, V6, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            codec4: GpuCodec<in P, V4>, getter4: (A) -> V4,
            codec5: GpuCodec<in P, V5>, getter5: (A) -> V5,
            codec6: GpuCodec<in P, V6>, getter6: (A) -> V6,
            constructor: (V0, V1, V2, V3, V4, V5, V6) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long
            val skip4: Long
            val skip5: Long
            val skip6: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                    .add(codec4)
                    .add(codec5)
                    .add(codec6)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
                skip4 = builder[4]
                skip5 = builder[5]
                skip6 = builder[6]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
                packing.encode(codec4, getter4(value), skip4, output)
                packing.encode(codec5, getter5(value), skip5, output)
                packing.encode(codec6, getter6(value), skip6, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input),
                    packing.decode(codec4, skip4, input),
                    packing.decode(codec5, skip5, input),
                    packing.decode(codec6, skip6, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, V4, V5, V6, V7, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            codec4: GpuCodec<in P, V4>, getter4: (A) -> V4,
            codec5: GpuCodec<in P, V5>, getter5: (A) -> V5,
            codec6: GpuCodec<in P, V6>, getter6: (A) -> V6,
            codec7: GpuCodec<in P, V7>, getter7: (A) -> V7,
            constructor: (V0, V1, V2, V3, V4, V5, V6, V7) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long
            val skip4: Long
            val skip5: Long
            val skip6: Long
            val skip7: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                    .add(codec4)
                    .add(codec5)
                    .add(codec6)
                    .add(codec7)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
                skip4 = builder[4]
                skip5 = builder[5]
                skip6 = builder[6]
                skip7 = builder[7]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
                packing.encode(codec4, getter4(value), skip4, output)
                packing.encode(codec5, getter5(value), skip5, output)
                packing.encode(codec6, getter6(value), skip6, output)
                packing.encode(codec7, getter7(value), skip7, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input),
                    packing.decode(codec4, skip4, input),
                    packing.decode(codec5, skip5, input),
                    packing.decode(codec6, skip6, input),
                    packing.decode(codec7, skip7, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, V4, V5, V6, V7, V8, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            codec4: GpuCodec<in P, V4>, getter4: (A) -> V4,
            codec5: GpuCodec<in P, V5>, getter5: (A) -> V5,
            codec6: GpuCodec<in P, V6>, getter6: (A) -> V6,
            codec7: GpuCodec<in P, V7>, getter7: (A) -> V7,
            codec8: GpuCodec<in P, V8>, getter8: (A) -> V8,
            constructor: (V0, V1, V2, V3, V4, V5, V6, V7, V8) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long
            val skip4: Long
            val skip5: Long
            val skip6: Long
            val skip7: Long
            val skip8: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                    .add(codec4)
                    .add(codec5)
                    .add(codec6)
                    .add(codec7)
                    .add(codec8)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
                skip4 = builder[4]
                skip5 = builder[5]
                skip6 = builder[6]
                skip7 = builder[7]
                skip8 = builder[8]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
                packing.encode(codec4, getter4(value), skip4, output)
                packing.encode(codec5, getter5(value), skip5, output)
                packing.encode(codec6, getter6(value), skip6, output)
                packing.encode(codec7, getter7(value), skip7, output)
                packing.encode(codec8, getter8(value), skip8, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input),
                    packing.decode(codec4, skip4, input),
                    packing.decode(codec5, skip5, input),
                    packing.decode(codec6, skip6, input),
                    packing.decode(codec7, skip7, input),
                    packing.decode(codec8, skip8, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, V4, V5, V6, V7, V8, V9, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            codec4: GpuCodec<in P, V4>, getter4: (A) -> V4,
            codec5: GpuCodec<in P, V5>, getter5: (A) -> V5,
            codec6: GpuCodec<in P, V6>, getter6: (A) -> V6,
            codec7: GpuCodec<in P, V7>, getter7: (A) -> V7,
            codec8: GpuCodec<in P, V8>, getter8: (A) -> V8,
            codec9: GpuCodec<in P, V9>, getter9: (A) -> V9,
            constructor: (V0, V1, V2, V3, V4, V5, V6, V7, V8, V9) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long
            val skip4: Long
            val skip5: Long
            val skip6: Long
            val skip7: Long
            val skip8: Long
            val skip9: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                    .add(codec4)
                    .add(codec5)
                    .add(codec6)
                    .add(codec7)
                    .add(codec8)
                    .add(codec9)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
                skip4 = builder[4]
                skip5 = builder[5]
                skip6 = builder[6]
                skip7 = builder[7]
                skip8 = builder[8]
                skip9 = builder[9]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
                packing.encode(codec4, getter4(value), skip4, output)
                packing.encode(codec5, getter5(value), skip5, output)
                packing.encode(codec6, getter6(value), skip6, output)
                packing.encode(codec7, getter7(value), skip7, output)
                packing.encode(codec8, getter8(value), skip8, output)
                packing.encode(codec9, getter9(value), skip9, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input),
                    packing.decode(codec4, skip4, input),
                    packing.decode(codec5, skip5, input),
                    packing.decode(codec6, skip6, input),
                    packing.decode(codec7, skip7, input),
                    packing.decode(codec8, skip8, input),
                    packing.decode(codec9, skip9, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            codec4: GpuCodec<in P, V4>, getter4: (A) -> V4,
            codec5: GpuCodec<in P, V5>, getter5: (A) -> V5,
            codec6: GpuCodec<in P, V6>, getter6: (A) -> V6,
            codec7: GpuCodec<in P, V7>, getter7: (A) -> V7,
            codec8: GpuCodec<in P, V8>, getter8: (A) -> V8,
            codec9: GpuCodec<in P, V9>, getter9: (A) -> V9,
            codec10: GpuCodec<in P, V10>, getter10: (A) -> V10,
            constructor: (V0, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long
            val skip4: Long
            val skip5: Long
            val skip6: Long
            val skip7: Long
            val skip8: Long
            val skip9: Long
            val skip10: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                    .add(codec4)
                    .add(codec5)
                    .add(codec6)
                    .add(codec7)
                    .add(codec8)
                    .add(codec9)
                    .add(codec10)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
                skip4 = builder[4]
                skip5 = builder[5]
                skip6 = builder[6]
                skip7 = builder[7]
                skip8 = builder[8]
                skip9 = builder[9]
                skip10 = builder[10]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
                packing.encode(codec4, getter4(value), skip4, output)
                packing.encode(codec5, getter5(value), skip5, output)
                packing.encode(codec6, getter6(value), skip6, output)
                packing.encode(codec7, getter7(value), skip7, output)
                packing.encode(codec8, getter8(value), skip8, output)
                packing.encode(codec9, getter9(value), skip9, output)
                packing.encode(codec10, getter10(value), skip10, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input),
                    packing.decode(codec4, skip4, input),
                    packing.decode(codec5, skip5, input),
                    packing.decode(codec6, skip6, input),
                    packing.decode(codec7, skip7, input),
                    packing.decode(codec8, skip8, input),
                    packing.decode(codec9, skip9, input),
                    packing.decode(codec10, skip10, input)
                )
            }
        }

        @JvmStatic
        fun <V0, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, P : GpuPacking, A> struct(
            codec0: GpuCodec<in P, V0>, getter0: (A) -> V0,
            codec1: GpuCodec<in P, V1>, getter1: (A) -> V1,
            codec2: GpuCodec<in P, V2>, getter2: (A) -> V2,
            codec3: GpuCodec<in P, V3>, getter3: (A) -> V3,
            codec4: GpuCodec<in P, V4>, getter4: (A) -> V4,
            codec5: GpuCodec<in P, V5>, getter5: (A) -> V5,
            codec6: GpuCodec<in P, V6>, getter6: (A) -> V6,
            codec7: GpuCodec<in P, V7>, getter7: (A) -> V7,
            codec8: GpuCodec<in P, V8>, getter8: (A) -> V8,
            codec9: GpuCodec<in P, V9>, getter9: (A) -> V9,
            codec10: GpuCodec<in P, V10>, getter10: (A) -> V10,
            codec11: GpuCodec<in P, V11>, getter11: (A) -> V11,
            constructor: (V0, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11) -> A,
            packing: P
        ) = object : GpuCodec<P, A> {
            override val alignment: Long
            override val packing: P = packing

            val skip0: Long
            val skip1: Long
            val skip2: Long
            val skip3: Long
            val skip4: Long
            val skip5: Long
            val skip6: Long
            val skip7: Long
            val skip8: Long
            val skip9: Long
            val skip10: Long
            val skip11: Long

            init {
                val builder = packing.structBuilder()
                    .add(codec0)
                    .add(codec1)
                    .add(codec2)
                    .add(codec3)
                    .add(codec4)
                    .add(codec5)
                    .add(codec6)
                    .add(codec7)
                    .add(codec8)
                    .add(codec9)
                    .add(codec10)
                    .add(codec11)
                alignment = builder.end()
                skip0 = builder[0]
                skip1 = builder[1]
                skip2 = builder[2]
                skip3 = builder[3]
                skip4 = builder[4]
                skip5 = builder[5]
                skip6 = builder[6]
                skip7 = builder[7]
                skip8 = builder[8]
                skip9 = builder[9]
                skip10 = builder[10]
                skip11 = builder[11]
            }

            override fun encode(value: A, output: MemoryWriter) {
                packing.encode(codec0, getter0(value), skip0, output)
                packing.encode(codec1, getter1(value), skip1, output)
                packing.encode(codec2, getter2(value), skip2, output)
                packing.encode(codec3, getter3(value), skip3, output)
                packing.encode(codec4, getter4(value), skip4, output)
                packing.encode(codec5, getter5(value), skip5, output)
                packing.encode(codec6, getter6(value), skip6, output)
                packing.encode(codec7, getter7(value), skip7, output)
                packing.encode(codec8, getter8(value), skip8, output)
                packing.encode(codec9, getter9(value), skip9, output)
                packing.encode(codec10, getter10(value), skip10, output)
                packing.encode(codec11, getter11(value), skip11, output)
            }

            override fun decode(input: MemoryReader): A {
                return constructor(
                    packing.decode(codec0, skip0, input),
                    packing.decode(codec1, skip1, input),
                    packing.decode(codec2, skip2, input),
                    packing.decode(codec3, skip3, input),
                    packing.decode(codec4, skip4, input),
                    packing.decode(codec5, skip5, input),
                    packing.decode(codec6, skip6, input),
                    packing.decode(codec7, skip7, input),
                    packing.decode(codec8, skip8, input),
                    packing.decode(codec9, skip9, input),
                    packing.decode(codec10, skip10, input),
                    packing.decode(codec11, skip11, input)
                )
            }
        }
    }
}