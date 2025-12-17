package net.typho.big_shot_lib.spirv

import net.typho.big_shot_lib.error.ShaderMixinException
import java.nio.ByteBuffer
import java.util.*

class ShaderMixinContext : Iterable<ShaderMixinContext.Opcode> {
    val code = LinkedList<ByteBuffer>()
    var headerSize = 5

    fun split(index: Int): Int? {
        var listIndex = 0
        var sectionStart = 0
        var sectionEnd = 0

        for (section in code) {
            sectionEnd += section.capacity()

            if (index == sectionStart) {
                return listIndex
            } else if (index == sectionEnd) {
                return listIndex + 1
            } else if (index > sectionStart && index < sectionEnd) {
                val lowSection = ByteBuffer.allocate(index - sectionStart)
                lowSection.put(0, section, 0, lowSection.capacity())

                val highSection = ByteBuffer.allocate(sectionEnd - index)
                highSection.put(0, section, lowSection.capacity(), highSection.capacity())

                return listIndex + 1
            }

            sectionStart = sectionEnd
            listIndex++
        }

        return null
    }

    fun inject(index: Int, inject: ByteBuffer) {
        code.add(split(index)!!, inject)
    }

    fun inject(at: At, inject: ByteBuffer) = inject(at.getStart(this), inject)

    fun compile(): ByteBuffer {
        if (code.isEmpty()) {
            val buffer = ByteBuffer.allocate(0)
            code.add(buffer)
            return buffer
        }

        if (code.size == 1) {
            return code[0]
        }

        var size = 0

        for (bytes in code) {
            size += bytes.capacity()
        }

        val array = ByteBuffer.allocate(size)
        var index = 0

        for (bytes in code) {
            array.put(index, bytes, 0, bytes.capacity())
            index += bytes.capacity()
        }

        code.clear()
        code.add(array)

        return array
    }

    fun getOpcodeData(op: Opcode): ByteBuffer {
        val code = compile()
        val buf = ByteBuffer.allocate(op.length * WORD_BYTES)
        buf.put(0, code, (op.index + 1) * WORD_BYTES, buf.capacity())
        return buf
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun iterator(): Iterator<Opcode> {
        return object : Iterator<Opcode> {
            val code = compile()
            var index = headerSize

            override fun hasNext() = index * WORD_BYTES < code.capacity()

            override fun next(): Opcode {
                val op = code.getInt(index * WORD_BYTES)
                val type = op and 0xFFFF
                val length = (op ushr 16) and 0xFFFF

                println("Op ${op.toHexString()} index $index, type $type, length $length")

                val opcode = Opcode(index, type, length)

                index += length

                return opcode
            }
        }
    }

    companion object {
        const val WORD_BYTES = 4
    }

    class Opcode(val index: Int, val type: Int, val length: Int)

    interface At {
        fun getStart(context: ShaderMixinContext): Int

        fun getEnd(context: ShaderMixinContext): Int? = null
    }

    open class AtOpcode(val id: Int) : At {
        override fun getStart(context: ShaderMixinContext): Int {
            for (opcode in context) {
                if (opcode.type == id) {
                    return opcode.index
                }
            }

            return 0
        }

        override fun getEnd(context: ShaderMixinContext): Int {
            for (opcode in context) {
                if (opcode.type == id) {
                    return opcode.index + opcode.length + 1
                }
            }

            return 0
        }
    }

    open class AtOpcodeInMethod(val id: Int, val method: String): At {
        override fun getStart(context: ShaderMixinContext): Int {
            var methodId: Int? = -1

            for (opcode in context) {
                if (opcode.type == 5) { // OpName
                    val contents = context.getOpcodeData(opcode)
                    val contentsArray = contents.array()
                    val name = String(contentsArray, WORD_BYTES, contentsArray.size - 2 * WORD_BYTES)

                    if (name.trim(0.toChar()) == method) {
                        methodId = contents.getInt(0)
                        break;
                    }
                }
            }

            if (methodId == -1) {
                throw ShaderMixinException("Unable to find method $method")
            }

            var inMethod = false

            for (opcode in context) {
                if (opcode.type == 54) { // OpFunction
                    if (inMethod) {
                        break
                    }

                    val contents = context.getOpcodeData(opcode)

                    if (contents.getInt(WORD_BYTES) == methodId) {
                        inMethod = true
                    }
                } else if (inMethod && opcode.type == id) {
                    println("injecting at ${opcode.index} $id")
                    return opcode.index
                }
            }

            throw ShaderMixinException("Unable to find opcode $id method $method")
        }
    }

    open class AtVoidReturn(method: String): AtOpcodeInMethod(253, method) // OpReturn

    open class AtReturnValue(method: String): AtOpcodeInMethod(254, method) // OpReturnValue
}