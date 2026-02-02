package net.typho.big_shot_lib.spirv

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.gl.resource.ShaderType

class ShaderLocationsInfo(
    format: VertexFormat?,
    @JvmField
    val hasGeometryShader: Boolean
) {
    @JvmField
    val uniforms = Mapper()
    @JvmField
    val vertexInputs = Mapper()
    @JvmField
    val vertexOutputs = Mapper()
    @JvmField
    val fragmentInputs = if (hasGeometryShader) Mapper() else vertexOutputs
    @JvmField
    val fragmentOutputs = Mapper()

    init {
        if (format != null) {
            var i = 0

            for (element in format.elements) {
                vertexInputs.map[format.getElementName(element)] = Mapper.Mapped(i++, 1)
            }
        }
    }

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
        data class Mapped(val location: Int, val size: Int) {
            fun overlaps(other: Mapped): Boolean {
                val aStart = location
                val aEnd = location + size - 1
                val bStart = other.location
                val bEnd = other.location + other.size - 1
                return aStart <= bEnd && bStart <= aEnd
            }
        }

        @JvmField
        val map = HashMap<String, Mapped>()

        fun findUnboundLocation(size: Int): Mapped {
            var check = 0

            while (true) {
                val mapped = Mapped(check, size)

                if (map.values.none { other -> mapped.overlaps(other) }) {
                    return mapped
                }

                check++
            }
        }

        fun map(id: String, size: Int, attempt: Int? = null): Int {
            return map.computeIfAbsent(id) { key ->
                if (attempt != null) {
                    val attemptMapped = Mapped(attempt, size)

                    for (entry in map) {
                        if (entry.value.overlaps(attemptMapped)) {
                            return@computeIfAbsent findUnboundLocation(size)
                        }
                    }

                    return@computeIfAbsent attemptMapped
                }

                return@computeIfAbsent findUnboundLocation(size)
            }.location
        }

        fun get(id: String) = map[id]?.location

        override fun toString() = map.toString()
    }
}