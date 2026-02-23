package net.typho.big_shot_lib.api.client.opengl.shaders.variables

import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderOpcode
import net.typho.big_shot_lib.api.util.buffers.BufferUtil.concat
import java.nio.IntBuffer

sealed interface ShaderVariableTypeInfo {
    fun matchesBytecode(opcode: ShaderOpcode, expectedId: Int? = null): Boolean

    fun findOrInjectBytecode(
        buffer: ShaderBytecodeBuffer,
        index: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index
    ): Int {
        buffer.findOpcode { matchesBytecode(it) }?.let {
            return it.getWord(0)
        }

        return injectBytecode(buffer, index)
    }

    fun injectBytecode(
        buffer: ShaderBytecodeBuffer,
        words: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index
    ): Int {
        val id = buffer.bound++
        buffer.insert(words, compileBytecode(id, buffer))
        return id
    }

    fun compileBytecode(id: Int, buffer: ShaderBytecodeBuffer): IntBuffer

    @JvmRecord
    data class Primitive(
        @JvmField
        val opcodeId: Int,
        @JvmField
        val widthBits: Int? = null,
        @JvmField
        val signed: Boolean? = null
    ) : ShaderVariableTypeInfo {
        override fun matchesBytecode(opcode: ShaderOpcode, expectedId: Int?): Boolean {
            return opcode.id == opcodeId && (expectedId == null || opcode.getWord(0) == expectedId)
        }

        override fun compileBytecode(id: Int, buffer: ShaderBytecodeBuffer): IntBuffer {
            return ShaderOpcode.Builder(opcodeId)
                .putWord(id)
                .putWord(widthBits)
                .putWord(signed?.let { if (it) 1 else 0 })
                .build()
        }
    }

    @JvmRecord
    data class Vector(
        @JvmField
        val component: ShaderVariableTypeInfo,
        @JvmField
        val size: Int
    ) : ShaderVariableTypeInfo {
        override fun matchesBytecode(opcode: ShaderOpcode, expectedId: Int?): Boolean {
            return opcode.id == ShaderOpcode.OP_TYPE_VECTOR
                    && (expectedId == null || opcode.getWord(0) == expectedId)
                    && opcode.getWord(2) == size
                    && opcode.getWord(1).let { componentId ->
                opcode.parent.opcodes().any { component.matchesBytecode(it, componentId) }
            }
        }

        override fun findOrInjectBytecode(buffer: ShaderBytecodeBuffer, index: Int): Int {
            for (opcode in buffer.opcodes()) {
                if (matchesBytecode(opcode)) {
                    return opcode.getWord(0)
                }
            }

            for (opcode in buffer.opcodes()) {
                if (component.matchesBytecode(opcode)) {
                    val id = buffer.bound++
                    val id1 = opcode.getWord(0)

                    buffer.insert(
                        index,
                        ShaderOpcode.Builder(ShaderOpcode.OP_TYPE_VECTOR)
                            .putWord(id)
                            .putWord(id1)
                            .putWord(size)
                            .build()
                    )
                    return id
                }
            }

            return injectBytecode(buffer, index)
        }

        override fun compileBytecode(id: Int, buffer: ShaderBytecodeBuffer): IntBuffer {
            val id1 = buffer.bound++
            return component.compileBytecode(id1, buffer).concat(
                ShaderOpcode.Builder(ShaderOpcode.OP_TYPE_VECTOR)
                    .putWord(id)
                    .putWord(id1)
                    .putWord(size)
                    .build()
            )
        }
    }

    @JvmRecord
    data class Matrix(
        @JvmField
        val component: ShaderVariableTypeInfo,
        @JvmField
        val size: Int
    ) : ShaderVariableTypeInfo {
        override fun matchesBytecode(opcode: ShaderOpcode, expectedId: Int?): Boolean {
            return opcode.id == ShaderOpcode.OP_TYPE_MATRIX
                    && (expectedId == null || opcode.getWord(0) == expectedId)
                    && opcode.getWord(2) == size
                    && opcode.getWord(1).let { componentId ->
                opcode.parent.opcodes().any { component.matchesBytecode(it, componentId) }
            }
        }

        override fun findOrInjectBytecode(buffer: ShaderBytecodeBuffer, index: Int): Int {
            for (opcode in buffer.opcodes()) {
                if (matchesBytecode(opcode)) {
                    return opcode.getWord(0)
                }
            }

            for (opcode in buffer.opcodes()) {
                if (component.matchesBytecode(opcode)) {
                    val id = buffer.bound++
                    val id1 = opcode.getWord(0)

                    buffer.insert(
                        index,
                        ShaderOpcode.Builder(ShaderOpcode.OP_TYPE_MATRIX)
                            .putWord(id)
                            .putWord(id1)
                            .putWord(size)
                            .build()
                    )
                    return id
                }
            }

            return injectBytecode(buffer, index)
        }

        override fun compileBytecode(id: Int, buffer: ShaderBytecodeBuffer): IntBuffer {
            val id1 = buffer.bound++
            return component.compileBytecode(id1, buffer).concat(
                ShaderOpcode.Builder(ShaderOpcode.OP_TYPE_MATRIX)
                    .putWord(id)
                    .putWord(id1)
                    .putWord(size)
                    .build()
            )
        }
    }

    object Image/*(
        @JvmField
        val component: ShaderVariableInfo,
        @JvmField
        val dimensionality: ImageDimensionality,
        @JvmField
        val depth: Boolean?,
        @JvmField
        val arrayed: Boolean,
        @JvmField
        val multiSample: Boolean,
        @JvmField
        val sampled: Boolean?
    )*/ : ShaderVariableTypeInfo {
        override fun matchesBytecode(
            opcode: ShaderOpcode,
            expectedId: Int?
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun compileBytecode(
            id: Int,
            buffer: ShaderBytecodeBuffer
        ): IntBuffer {
            TODO("Not yet implemented")
            /*
            val id1 = buffer.bound++
            return component.compileBytecode(id1, buffer).concat(
                ShaderOpcode.Builder(ShaderOpcode.OP_TYPE_IMAGE)
                    .putWord(id)
                    .putWord(id1)
                    .putWord(dimensionality.id)
                    .putWord(depth?.let { if (it) 1 else 0 } ?: 2)
                    .putWord(if (arrayed) 1 else 0)
                    .putWord(if (multiSample) 1 else 0)
                    .putWord(sampled?.let { if (it) 2 else 1 } ?: 0)
                    .putWord(0) // Unknown
                    .build()
            )
             */
        }
    }

    object Sampler : ShaderVariableTypeInfo {
        override fun matchesBytecode(
            opcode: ShaderOpcode,
            expectedId: Int?
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun compileBytecode(
            id: Int,
            buffer: ShaderBytecodeBuffer
        ): IntBuffer {
            TODO("Not yet implemented")
        }
    }
}