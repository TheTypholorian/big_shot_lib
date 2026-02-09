package net.typho.big_shot_lib.api.shaders.mixins

import net.typho.big_shot_lib.api.errors.InvalidEnumException
import net.typho.big_shot_lib.api.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.shaders.ShaderSourceType

open class ShaderLocationManager(
    @JvmField
    val key: ShaderProgramKey
) {
    @JvmField
    val uniforms = Mapper()
    @JvmField
    val vertexInputs = Mapper()
    @JvmField
    val vertexOutputs = Mapper()
    @JvmField
    val fragmentInputs = if (key.sources.contains(ShaderSourceType.GEOMETRY)) Mapper() else vertexOutputs
    @JvmField
    val fragmentOutputs = Mapper()

    init {
        if (key.loader.autoBindLocations) {
            key.format.elements.forEachIndexed { index, element ->
                vertexInputs.setOrExpand(index, key.format.getElementName(element))
            }
        }
    }

    fun getMapper(storageClass: ShaderStorageClass, type: ShaderSourceType): Mapper? {
        return when (storageClass) {
            ShaderStorageClass.UNIFORM, ShaderStorageClass.UNIFORM_CONSTANT -> uniforms
            ShaderStorageClass.INPUT -> when (type) {
                ShaderSourceType.VERTEX -> vertexInputs
                ShaderSourceType.GEOMETRY -> vertexOutputs
                ShaderSourceType.FRAGMENT -> fragmentInputs
                else -> throw InvalidEnumException(type.toString())
            }
            ShaderStorageClass.OUTPUT -> when (type) {
                ShaderSourceType.VERTEX -> vertexOutputs
                ShaderSourceType.GEOMETRY -> fragmentInputs
                ShaderSourceType.FRAGMENT -> fragmentOutputs
                else -> throw InvalidEnumException(type.toString())
            }
        }
    }

    open class Mapper {
        @JvmField
        val locations = ArrayList<String?>()

        fun setOrExpand(index: Int, value: String?) {
            while (index >= locations.size) {
                locations.add(null)
            }

            locations[index] = value
        }

        fun map(size: Int, name: String): Int {
            var foundLocation: Int? = null
            var foundSize: Int? = null

            fun pick(): Int {
                for (i in foundLocation!! until foundLocation!! + size) {
                    setOrExpand(i, name)
                }

                return foundLocation!!
            }

            locations.forEachIndexed { location, bound ->
                if (bound == null) {
                    if (foundSize == null) {
                        foundLocation = location
                        foundSize = 0
                    }

                    foundSize++

                    if (foundSize >= size) {
                        return pick()
                    }
                } else if (bound == name) {
                    return location
                } else {
                    foundLocation = null
                    foundSize = null
                }
            }

            if (foundLocation == null) {
                foundLocation = locations.size
            }

            return pick()
        }
    }
}