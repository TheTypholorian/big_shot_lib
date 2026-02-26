package net.typho.big_shot_lib.api.client.opengl.shaders.mixins

import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.errors.InvalidEnumException

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

        fun expand(index: Int) {
            while (index >= locations.size) {
                locations.add(null)
            }
        }

        fun setOrExpand(index: Int, value: String?) {
            expand(index)

            locations[index] = value
        }

        fun get(name: String): Int? {
            locations.forEachIndexed { location, bound ->
                if (name == bound) {
                    return location
                }
            }

            return null
        }

        fun map(size: Int, name: String, tryLocation: Int? = null): Int {
            fun pick(loc: Int): Int {
                for (i in loc until loc + size) {
                    setOrExpand(i, name)
                }

                return loc
            }

            if (tryLocation != null) {
                expand(tryLocation + size)

                if (locations.subList(tryLocation, tryLocation + size).filterNotNull().isEmpty()) {
                    return pick(tryLocation)
                }
            }

            var foundLocation: Int? = null
            var foundSize: Int? = null

            locations.forEachIndexed { location, bound ->
                if (bound == null) {
                    if (foundSize == null) {
                        foundLocation = location
                        foundSize = 0
                    }

                    foundSize++

                    if (foundSize >= size) {
                        return pick(foundLocation!!)
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

            return pick(foundLocation)
        }
    }
}