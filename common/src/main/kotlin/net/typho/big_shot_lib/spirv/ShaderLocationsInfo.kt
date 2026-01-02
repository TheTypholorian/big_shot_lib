package net.typho.big_shot_lib.spirv

import net.typho.big_shot_lib.gl.resource.ShaderType

class ShaderLocationsInfo(val hasGeometryShader: Boolean) {
    val uniforms = Mapper()
    val vertexInputs = Mapper()
    val vertexOutputs = Mapper()
    val fragmentInputs = if (hasGeometryShader) Mapper() else vertexOutputs
    val fragmentOutputs = Mapper()

    fun getMapper(storageClass: Int, stage: ShaderType): Mapper? {
        if (stage == ShaderType.GEOMETRY && !hasGeometryShader) {
            throw IllegalStateException("Created a ShaderLocationsInfo with no geometry shader but attempted to map one anyway")
        }

        return when (storageClass) {
            0, 2 -> uniforms
            1 -> when (stage) {
                ShaderType.VERTEX -> vertexInputs
                ShaderType.GEOMETRY -> vertexOutputs
                ShaderType.FRAGMENT -> fragmentInputs
            }
            3 -> when (stage) {
                ShaderType.VERTEX -> vertexOutputs
                ShaderType.GEOMETRY -> fragmentInputs
                ShaderType.FRAGMENT -> fragmentOutputs
            }
            else -> null
        }
    }

    class Mapper {
        val map = HashMap<String, Int>()

        fun findUnboundLocation(): Int {
            repeat(map.size) { i ->
                if (!map.values.contains(i)) {
                    return i
                }
            }

            return map.size
        }

        fun map(id: String, attempt: Int? = null): Int {
            return map.computeIfAbsent(id) { key ->
                if (attempt != null) {
                    for (entry in map) {
                        if (entry.value == attempt) {
                            return@computeIfAbsent findUnboundLocation()
                        }
                    }

                    return@computeIfAbsent attempt
                }

                return@computeIfAbsent findUnboundLocation()
            }
        }

        override fun toString() = map.toString()
    }
}