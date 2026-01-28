package net.typho.big_shot_lib.shaders.mixins

import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.errors.ShaderMixinException
import net.typho.big_shot_lib.shaders.mixins.variables.ShaderAnyType
import net.typho.big_shot_lib.shaders.mixins.variables.ShaderVariable
import net.typho.big_shot_lib.shaders.mixins.variables.ShaderVariableType
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ShaderMixinContext(@JvmField var code: ByteBuffer) : Iterable<Opcode> {
    companion object {
        const val WORD_SIZE_BYTES = 4
        @JvmField
        val BYTE_ORDER: ByteOrder = ByteOrder.LITTLE_ENDIAN
    }

    @JvmField
    var headerSize = 5
    @JvmField
    var bound: Int = 0

    init {
        code.order(BYTE_ORDER)
        loadBound()
    }

    fun loadBound() {
        bound = code.getInt(3 * WORD_SIZE_BYTES)
    }

    fun putBound() {
        if (bound <= 0) {
            BigShotLib.LOGGER.warn("Shader Mixin Context bound $bound is a suspicious value, did you remember to call loadBound() after initializing?")
        }

        code.putInt(3 * WORD_SIZE_BYTES, bound)
    }

    fun split(words: Int, size: Int) {
        val bytes = words * WORD_SIZE_BYTES
        val newBuffer = ByteBuffer.allocate(code.capacity() + size).order(BYTE_ORDER)

        if (bytes == 0) {
            newBuffer.put(size, code, 0, code.capacity())
        } else if (bytes > 0 && bytes < code.capacity()) {
            newBuffer.put(0, code, 0, bytes)
            newBuffer.put(bytes + size, code, bytes, code.capacity() - bytes)
        } else {
            newBuffer.put(0, code, 0, code.capacity())
        }

        code = newBuffer
    }

    fun splice(words: Int, size: Int) {
        val bytes = words * WORD_SIZE_BYTES
        val newBuffer = ByteBuffer.allocate(code.capacity() - size).order(BYTE_ORDER)

        newBuffer.put(0, code, 0, bytes)
        newBuffer.put(bytes, code, bytes + size, code.capacity() - (bytes + size))

        code = newBuffer
    }

    fun inject(words: Int, vararg inject: ByteBuffer) {
        var bytes = words * WORD_SIZE_BYTES
        var size = 0

        for (buffer in inject) {
            size += buffer.capacity()
        }

        if (size != 0) {
            split(words, size)

            for (buffer in inject) {
                code.put(bytes, buffer, 0, buffer.capacity())
                bytes += buffer.capacity()
            }
        }
    }

    fun addStaticVar(storageClass: Int, typeId: Int, name: String? = null, initializer: Int? = null): ShaderVariable {
        val typePointerId = bound++
        val variableId = bound++

        inject(
            locateOpcode(Opcode.OP_FUNCTION)!!.index,
            Opcode.Builder(Opcode.OP_TYPE_POINTER)
                .putInt(typePointerId)
                .putInt(storageClass)
                .putInt(typeId)
                .build(),
            Opcode.Builder(Opcode.OP_VARIABLE)
                .putInt(typePointerId)
                .putInt(variableId)
                .putInt(storageClass)
                .putInt(initializer)
                .build()
        )

        name?.let {
            inject(
                locateOpcode(Opcode.OP_NAME)!!.index,
                Opcode.Builder(Opcode.OP_NAME)
                    .putInt(variableId)
                    .putString(name)
                    .build()
            )
        }

        putBound()
        return ShaderVariable(
            variableId,
            name,
            null,
            typePointerId,
            typeId
        )
    }

    fun addEntrypointVars(vararg ids: Int): Boolean {
        for (opcode in this) {
            if (opcode.id == Opcode.OP_ENTRY_POINT) { // OpEntryPoint
                code.putInt(
                    opcode.index * WORD_SIZE_BYTES,
                    ((opcode.length + ids.size) shl 16) or opcode.id
                )
                val buffer = ByteBuffer.allocate(ids.size * WORD_SIZE_BYTES)
                        .order(BYTE_ORDER)
                buffer.asIntBuffer().put(ids)
                inject(
                    opcode.index + opcode.length,
                    buffer
                )
                return true
            }
        }

        return false
    }

    fun locateOpcode(id: Int): Opcode? {
        for (opcode in this) {
            if (opcode.id == id) {
                return opcode
            }
        }

        return null
    }

    fun locateOpcodeInMethod(id: Int, method: String): Opcode? {
        var methodId: Int? = -1

        for (opcode in this) {
            if (opcode.id == Opcode.OP_NAME) {
                val contents = getOpcodeData(opcode)
                val contentsArray = contents.array()
                val name = String(contentsArray,
                    WORD_SIZE_BYTES, contentsArray.size - 2 * WORD_SIZE_BYTES
                )

                if (name.trim(0.toChar()) == method) {
                    methodId = contents.getInt(0)
                    break
                }
            }
        }

        if (methodId == -1) {
            throw ShaderMixinException("Unable to find method $method")
        }

        var inMethod = false

        for (opcode in this) {
            if (opcode.id == Opcode.OP_FUNCTION) {
                if (inMethod) {
                    break
                }

                val contents = getOpcodeData(opcode)

                if (contents.getInt(WORD_SIZE_BYTES) == methodId) {
                    inMethod = true
                }
            } else if (inMethod && opcode.id == id) {
                return opcode
            }
        }

        return null
    }

    fun locateVariable(
        name: String? = null,
        location: Int? = null,
        type: ShaderVariableType = ShaderAnyType
    ): ShaderVariable? {
        for (opcode in this) {
            if (opcode.id == Opcode.OP_VARIABLE) {
                val id = code.getInt((opcode.index + 2) * WORD_SIZE_BYTES)
                var actualName: String? = null
                var actualLocation: Int? = null
                var actualTypePointer: Int? = null
                var actualType: Int? = null

                for (opcode1 in this) {
                    if (opcode1.id == Opcode.OP_NAME) {
                        if (code.getInt((opcode1.index + 1) * WORD_SIZE_BYTES) == id) {
                            val contents = getOpcodeData(opcode1)
                            val contentsArray = contents.array()
                            actualName = String(
                                contentsArray,
                                WORD_SIZE_BYTES,
                                contentsArray.size - 2 * WORD_SIZE_BYTES
                            ).trim(0.toChar())
                            break
                        }
                    }
                }

                if (name != null && actualName != name) {
                    continue
                }

                for (opcode1 in this) {
                    if (opcode1.id == Opcode.OP_DECORATE) {
                        if (code.getInt((opcode1.index + 1) * WORD_SIZE_BYTES) == id) {
                            val decoration = code.getInt((opcode1.index + 2) * WORD_SIZE_BYTES)

                            if (decoration == 30) { // Location
                                actualLocation = code.getInt((opcode1.index + 3) * WORD_SIZE_BYTES)
                                break
                            }
                        }
                    }
                }

                if (location != null && actualLocation != location) {
                    continue
                }

                for (opcode1 in this) {
                    if (opcode1.id == Opcode.OP_VARIABLE) {
                        if (code.getInt((opcode1.index + 2) * WORD_SIZE_BYTES) == id) {
                            actualTypePointer = code.getInt((opcode1.index + 1) * WORD_SIZE_BYTES)
                            break
                        }
                    }
                }

                actualTypePointer!!

                for (opcode1 in this) {
                    if (opcode1.id == Opcode.OP_TYPE_POINTER) {
                        if (code.getInt((opcode1.index + 1) * WORD_SIZE_BYTES) == actualTypePointer) {
                            actualType = code.getInt((opcode1.index + 3) * WORD_SIZE_BYTES)
                            break
                        }
                    }
                }

                actualType!!

                val typeMatches = this.any { opcode1 ->
                    type.matches(opcode1, this, actualType)
                }

                if (!typeMatches) {
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

    fun getOpcodeData(op: Opcode): ByteBuffer {
        val buf = ByteBuffer.allocate(op.length * WORD_SIZE_BYTES)
        buf.put(0, code, (op.index + 1) * WORD_SIZE_BYTES, buf.capacity())
        return buf
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun iterator(): Iterator<Opcode> {
        return object : Iterator<Opcode> {
            val code = this@ShaderMixinContext.code
            var index = headerSize

            override fun hasNext() = index * WORD_SIZE_BYTES < code.capacity()

            override fun next(): Opcode {
                val op = code.getInt(index * WORD_SIZE_BYTES)
                val type = op and 0xFFFF
                val length = (op ushr 16) and 0xFFFF
                val opcode = Opcode(index, type, length)

                index += length

                return opcode
            }
        }
    }
}