package net.typho.big_shot_lib.api.shaders.mixins

import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.util.function.Predicate

class ShaderBytecodeBuffer(
    @JvmField
    var buffer: IntBuffer
) {
    constructor(buffer: ByteBuffer) : this(buffer.asIntBuffer())

    @JvmField
    var headerSize = 5
    var bound: Int
        get() = buffer.get(3)
        set(value) {
            buffer.put(3, value)
        }

    fun insert(index: Int, vararg newContents: IntBuffer) = splice(index, index, *newContents)

    fun splice(start: Int, end: Int, vararg newContents: IntBuffer) {
        val insertSize = newContents.sumOf { buffer -> buffer.capacity() }
        val newSize = insertSize + start + (buffer.capacity() - end)
        val newBuffer = ByteBuffer.allocateDirect(newSize * Int.SIZE_BYTES).asIntBuffer()

        newBuffer.put(0, buffer, 0, start)

        var ptr = start

        for (insert in newContents) {
            newBuffer.put(ptr, insert, 0, insert.capacity())
            ptr += insert.capacity()
        }

        newBuffer.put(ptr, buffer, end, (buffer.capacity() - end))
        buffer = newBuffer
    }

    fun findOpcode(
        predicate: Predicate<ShaderOpcode>
    ): ShaderOpcode? {
        for (opcode in opcodes()) {
            if (predicate.test(opcode)) {
                return opcode
            }
        }

        return null
    }

    fun findOpcode(
        id: Int,
        predicate: Predicate<ShaderOpcode>
    ) = findOpcode(Predicate<ShaderOpcode> { opcode -> opcode.id == id }.and(predicate))

    fun findOpcode(
        id: Int,
        vararg expectedValues: Pair<Int, Int>
    ) = findOpcode(id) { opcode ->
        for (pair in expectedValues) {
            if (opcode.getWord(pair.first) != pair.second) {
                return@findOpcode false
            }
        }

        return@findOpcode true
    }

    fun ensureDirect(): IntBuffer {
        if (buffer.isDirect) {
            return buffer
        } else {
            val direct = ByteBuffer.allocateDirect(buffer.capacity() * Int.SIZE_BYTES)
                .asIntBuffer()
                .put(buffer)
                .flip()
            buffer = direct
            return direct
        }
    }

    fun opcodes(): Iterable<ShaderOpcode> {
        return object : Iterable<ShaderOpcode> {
            override fun iterator(): Iterator<ShaderOpcode> {
                return object : Iterator<ShaderOpcode> {
                    var index = headerSize

                    override fun hasNext() = index < buffer.capacity()

                    override fun next(): ShaderOpcode {
                        val op = buffer.get(index)
                        val type = op and 0xFFFF
                        val length = (op ushr 16) and 0xFFFF
                        val opcode = ShaderOpcode(this@ShaderBytecodeBuffer, index, type, length)

                        index += length

                        return opcode
                    }
                }
            }
        }
    }
}