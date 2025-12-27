package net.typho.big_shot_lib.spirv

import net.typho.big_shot_lib.gl.resource.ShaderType

class ShaderLocationsInfo {
    val outputs = HashMap<ShaderType, Mapper>()
    val inputs = Mapper()
    val uniforms = Mapper()

    fun getMapper(storageClass: Int, stage: ShaderType): Mapper? {
        return when (storageClass) {
            0, 2 -> uniforms
            1 -> {
                if (stage.ordinal == 0) {
                    inputs
                } else {
                    outputs.computeIfAbsent(ShaderType.entries[stage.ordinal - 1]) { Mapper() }
                }
            }
            3 -> outputs.computeIfAbsent(stage) { Mapper() }
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