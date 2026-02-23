package net.typho.big_shot_lib.api.client.opengl.shaders.mixins

import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceKey

object ShaderLocationMapperMixin : ShaderMixin.Factory<ShaderLocationMapperMixin.Instance> {
    override fun create(key: ShaderProgramKey, parent: ShaderMixinManager.Instance): Instance {
        return Instance(ShaderLocationManager(key))
    }

    open class Instance(
        @JvmField
        val locations: ShaderLocationManager
    ) : ShaderMixin {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            for (opDecorate in code.opcodes().filter { it.id == ShaderOpcode.OP_DECORATE && it.getWord(1) == 30 }) {
                val id = opDecorate.getWord(0)

                val opVariable = code.findOpcode(ShaderOpcode.OP_VARIABLE, 1 to id) ?: continue
                val typePointer = opVariable.getWord(0)
                val storageClassId = opVariable.getWord(2)

                ShaderStorageClass.entries.firstOrNull { it.id == storageClassId }?.let { storageClass ->
                    locations.getMapper(storageClass, key.type)?.let { mapper ->
                        val opName = code.findOpcode(ShaderOpcode.OP_NAME, 0 to id)
                            ?: throw IllegalStateException("Shader variable id $id of storage class $storageClass in $key is missing an OpName")
                        val name = opName.getString(1)

                        val opTypePointer = code.findOpcode(ShaderOpcode.OP_TYPE_POINTER, 0 to typePointer)
                            ?: throw IllegalStateException("Couldn't find type pointer $typePointer in shader $key, this should never happen")
                        val type = opTypePointer.getWord(2)

                        val size = code.findOpcode(ShaderOpcode.OP_TYPE_MATRIX, 0 to type)?.getWord(1) ?: 1

                        opDecorate.putWord(2, mapper.map(size, name))
                    }
                }
            }

            var offset = 0

            code.opcodes().filter { opcode -> opcode.id == ShaderOpcode.OP_DECORATE && opcode.getWord(1) == 33 }
                .forEach { opcode ->
                    val start = opcode.index - offset
                    code.splice(start, start + opcode.length)
                    offset += opcode.length
                }

            return code
        }
    }
}