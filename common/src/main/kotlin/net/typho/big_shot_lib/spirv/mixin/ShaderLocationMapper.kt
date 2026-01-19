package net.typho.big_shot_lib.spirv.mixin

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.ShaderType
import net.typho.big_shot_lib.spirv.Opcode
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo
import net.typho.big_shot_lib.spirv.ShaderMixinCallback
import net.typho.big_shot_lib.spirv.ShaderMixinContext
import net.typho.big_shot_lib.spirv.ShaderMixinContext.Companion.WORD_SIZE_BYTES
import java.util.*

object ShaderLocationMapper : ShaderMixinCallback {
    override fun mixinPostCompile(
        shader: ResourceLocation,
        type: ShaderType,
        format: VertexFormat?,
        context: ShaderMixinContext,
        locations: ShaderLocationsInfo
    ) {
        for (opcode in context) {
            if (opcode.id == Opcode.OP_DECORATE) {
                val id = context.code.getInt((opcode.index + 1) * WORD_SIZE_BYTES)

                for (opcode1 in context) {
                    if (opcode1.id == Opcode.OP_VARIABLE) {
                        val checkId = context.code.getInt((opcode1.index + 2) * WORD_SIZE_BYTES)

                        if (checkId == id) {
                            val decoration = context.code.getInt((opcode.index + 2) * WORD_SIZE_BYTES)

                            if (decoration == 30) { // Location
                                val storageClass = context.code.getInt((opcode1.index + 3) * WORD_SIZE_BYTES)

                                locations.getMapper(storageClass, type)?.let { mapper ->
                                    for (opcode2 in context) {
                                        if (opcode2.id == Opcode.OP_NAME) {
                                            val checkId1 = context.code.getInt((opcode2.index + 1) * WORD_SIZE_BYTES)

                                            if (checkId1 == id) {
                                                val contents = context.getOpcodeData(opcode2)
                                                val contentsArray = contents.array()
                                                val name = String(
                                                    contentsArray,
                                                    WORD_SIZE_BYTES,
                                                    contentsArray.size - 2 * WORD_SIZE_BYTES
                                                ).trim(0.toChar())

                                                val typePointer = context.code.getInt((opcode1.index + 1) * WORD_SIZE_BYTES)

                                                for (opcode3 in context) {
                                                    if (opcode3.id == Opcode.OP_TYPE_POINTER) {
                                                        val checkTypePointer = context.code.getInt((opcode3.index + 1) * WORD_SIZE_BYTES)

                                                        if (checkTypePointer == typePointer) {
                                                            val type = context.code.getInt((opcode3.index + 3) * WORD_SIZE_BYTES)
                                                            var size = 1

                                                            for (opcode4 in context) {
                                                                if (opcode4.id == Opcode.OP_TYPE_MATRIX) {
                                                                    val checkType = context.code.getInt((opcode4.index + 1) * WORD_SIZE_BYTES)

                                                                    if (checkType == type) {
                                                                        size = context.code.getInt((opcode4.index + 2) * WORD_SIZE_BYTES)
                                                                        break
                                                                    }
                                                                }
                                                            }

                                                            val index = (opcode.index + 3) * WORD_SIZE_BYTES
                                                            val location = context.code.getInt(index)
                                                            context.code.putInt(index, mapper.map(name, size, location))

                                                            return@let
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    throw IllegalStateException("Shader variable of storage class $storageClass in $type shader of program $shader is missing an OpName, fix that")
                                }

                                break
                            }
                        }
                    }
                }
            }
        }

        val toRemove = LinkedList<Opcode>()

        for (opcode in context) {
            if (opcode.id == Opcode.OP_DECORATE) {
                val decoration = context.code.getInt((opcode.index + 2) * WORD_SIZE_BYTES)

                if (decoration == 33) { // Binding
                    toRemove.add(opcode)
                }
            }
        }

        var offset = 0

        for (opcode in toRemove) {
            context.splice(opcode.index - offset, opcode.length * WORD_SIZE_BYTES)
            offset += opcode.length
        }
    }
}