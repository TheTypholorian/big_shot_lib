package net.typho.big_shot_lib.spirv.mixin

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.ShaderType
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo
import net.typho.big_shot_lib.spirv.ShaderMixinCallback
import net.typho.big_shot_lib.spirv.ShaderMixinContext
import net.typho.big_shot_lib.spirv.ShaderMixinContext.Companion.WORD_SIZE_BYTES

object ShaderLocationMapper : ShaderMixinCallback {
    override fun mixinSpirV(
        shader: ResourceLocation,
        type: ShaderType,
        format: VertexFormat?,
        context: ShaderMixinContext,
        locations: ShaderLocationsInfo
    ) {
        for (opcode in context) {
            if (opcode.id == 71) { // OpDecorate
                val id = context.code.getInt((opcode.index + 1) * WORD_SIZE_BYTES)

                for (opcode1 in context) {
                    if (opcode1.id == 59) { // OpVariable
                        val checkId = context.code.getInt((opcode1.index + 2) * WORD_SIZE_BYTES)

                        if (checkId == id) {
                            val decoration = context.code.getInt((opcode.index + 2) * WORD_SIZE_BYTES)

                            if (decoration == 30) { // Location
                                val storageClass = context.code.getInt((opcode1.index + 3) * WORD_SIZE_BYTES)

                                locations.getMapper(storageClass, type)?.let { mapper ->
                                    var name: String? = null

                                    for (opcode2 in context) {
                                        if (opcode2.id == 5) { // OpName
                                            val checkId1 = context.code.getInt((opcode2.index + 1) * WORD_SIZE_BYTES)

                                            if (checkId1 == id) {
                                                val contents = context.getOpcodeData(opcode2)
                                                val contentsArray = contents.array()
                                                name = String(
                                                    contentsArray,
                                                    WORD_SIZE_BYTES,
                                                    contentsArray.size - 2 * WORD_SIZE_BYTES
                                                ).trim(0.toChar())
                                                break
                                            }
                                        }
                                    }

                                    if (name == null) {
                                        throw IllegalStateException("Shader variable of storage class $storageClass in $type shader of program $shader is missing an OpName, fix that")
                                    }

                                    val index = (opcode.index + 3) * WORD_SIZE_BYTES
                                    val location = context.code.getInt(index)
                                    context.code.putInt(index, mapper.map(name, location))
                                }

                                break
                            }
                        }
                    }
                }
            }
        }
    }
}