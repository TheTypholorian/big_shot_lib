package net.typho.big_shot_lib.api.shaders.mixins

import net.typho.big_shot_lib.api.shaders.errors.ShaderOpcodeNotFoundException
import net.typho.big_shot_lib.api.shaders.variables.ShaderVariable
import net.typho.big_shot_lib.api.shaders.variables.ShaderVariableType
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

    fun findVariable(
        name: String? = null,
        location: Int? = null,
        type: ShaderVariableType? = null
    ): ShaderVariable? {
        for (opcode in opcodes()) {
            if (opcode.id == ShaderOpcode.OP_VARIABLE) {
                val id = opcode.getWord(1)
                val actualName = findOpcode(ShaderOpcode.OP_NAME, 0 to id)?.getString(1) ?: continue
                val actualLocation = findOpcode(ShaderOpcode.OP_NAME, 0 to id, 1 to 30)?.getWord(2) ?: continue
                val actualTypePointer = findOpcode(ShaderOpcode.OP_VARIABLE, 1 to id)?.getWord(0) ?: continue
                val actualType = findOpcode(ShaderOpcode.OP_TYPE_POINTER, 0 to actualTypePointer)?.getWord(2) ?: continue

                if (name != null && actualName != name) {
                    continue
                }

                if (location != null && actualLocation != location) {
                    continue
                }

                if (type != null && !opcodes().any { type.matches(it, actualType) }) {
                    continue
                }

                return ShaderVariable(
                    id,
                    actualName,
                    actualLocation,
                    actualTypePointer,
                    actualType
                )
            }
        }

        return null
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

    fun addEntrypointVars(vararg ids: Int) {
        val opcode = findOpcode(ShaderOpcode.OP_ENTRY_POINT)
            ?: throw ShaderOpcodeNotFoundException("Unable to find OP_ENTRY_POINT to inject entrypoint variables")

        opcode.putWord(0, ((opcode.length + ids.size) shl 16) or opcode.id)
        insert(
            opcode.index + opcode.length,
            ByteBuffer.allocate(ids.size * Int.SIZE_BYTES)
                .order(ShaderMixinManager.BYTE_ORDER)
                .asIntBuffer()
                .put(ids)
        )
    }

    fun addStaticVar(
        storageClass: Int,
        typeId: Int,
        name: String? = null,
        initializer: Int? = null
    ): ShaderVariable {
        val typePointerId = bound++
        val variableId = bound++

        insert(
            findOpcode(ShaderOpcode.OP_FUNCTION)!!.index,
            ShaderOpcode.Builder(ShaderOpcode.OP_TYPE_POINTER)
                .putWord(typePointerId)
                .putWord(storageClass)
                .putWord(typeId)
                .build(),
            ShaderOpcode.Builder(ShaderOpcode.OP_VARIABLE)
                .putWord(typePointerId)
                .putWord(variableId)
                .putWord(storageClass)
                .putWord(initializer)
                .build()
        )

        name?.let {
            insert(
                findOpcode(ShaderOpcode.OP_NAME)!!.index,
                ShaderOpcode.Builder(ShaderOpcode.OP_NAME)
                    .putWord(variableId)
                    .putString(name)
                    .build()
            )
        }

        return ShaderVariable(
            variableId,
            name,
            null,
            typePointerId,
            typeId
        )
    }
}