package net.typho.big_shot_lib.impl.util

import net.minecraft.client.renderer.RenderPipelines
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexFormat
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.*
import net.typho.big_shot_lib.api.client.opengl.shaders.variables.ShaderVariableType
import net.typho.big_shot_lib.api.client.util.dynamic_buffers.AlbedoDynamicBuffer
import net.typho.big_shot_lib.api.util.NeoCollections
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

class AlbedoDynamicBufferImpl : AlbedoDynamicBuffer.Impl {
    val builtin = NeoCollections.flatListOf<ResourceIdentifier>(
        RenderPipelines.getStaticPipelines()
            .filter { it.vertexShader.toNeo().equals("minecraft", "core/terrain") && it.fragmentShader.toNeo().equals("minecraft", "core/terrain") }
            .map { it.location.toNeo() }
    )

    override fun create(
        key: ShaderProgramKey,
        parent: ShaderMixinManager.Instance,
        location: Int?
    ): AlbedoDynamicBuffer.MixinInstance? {
        if (key.disabledDynamicBuffers.contains(AlbedoDynamicBuffer.location())) {
            return null
        }

        if (location == null) {
            BigShotApi.LOGGER.warn("Location for ${AlbedoDynamicBuffer.location()} is null when compiling $key, skipping")
            return null
        }

        if (key.location.equals("sodium", "blocks/block_layer_opaque")) {
            return SodiumMixin(location, (parent.getOrCreateMixinInstance(ShaderLocationMapperMixin) as ShaderLocationMapperMixin.Instance).locations)
        }

        if (key.builtinDynamicBuffers.contains(AlbedoDynamicBuffer.location()) || builtin.contains(key.location)) {
            return BuiltinMixin(location)
        }

        if (!key.format.contains(NeoVertexFormat.Element.TEXTURE_UV)) {
            return null
        }

        if (key.sources.contains(ShaderSourceType.GEOMETRY)) {
            BigShotApi.LOGGER.warn("${AlbedoDynamicBuffer.location()} currently doesn't support geometry shaders, skipping $key")
            return null
        }

        if (key.format.contains(NeoVertexFormat.Element.COLOR)) {
            return ColorMixin(location, (parent.getOrCreateMixinInstance(ShaderLocationMapperMixin) as ShaderLocationMapperMixin.Instance).locations)
        }

        return Mixin(location, (parent.getOrCreateMixinInstance(ShaderLocationMapperMixin) as ShaderLocationMapperMixin.Instance).locations)
    }

    class BuiltinMixin(
        override val fragLocation: Int
    ) : AlbedoDynamicBuffer.MixinInstance {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.FRAGMENT) {
                code.findVariable(name = AlbedoDynamicBuffer.FRAGMENT_VAR_NAME)?.let {
                    code.findOpcode(ShaderOpcode.OP_DECORATE, 0 to it.id, 1 to 30)?.putWord(2, fragLocation)
                }
            }

            return code
        }
    }

    class SodiumMixin(
        override val fragLocation: Int,
        @JvmField
        val locationMapper: ShaderLocationManager
    ) : AlbedoDynamicBuffer.MixinInstance {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.FRAGMENT) {
                val vec4 = ShaderVariableType.FLOAT_VEC4.findOrInjectBytecode(code)

                val output = code.addStaticVar(ShaderStorageClass.OUTPUT, vec4, AlbedoDynamicBuffer.FRAGMENT_VAR_NAME)
                code.setVariableLocation(output.id, fragLocation)

                val mul = code.findOpcodeInMethod("main", ShaderOpcode.OP_F_MUL)!!
                val temp = code.bound++
                code.insert(
                    mul.index,
                    ShaderOpcode.Builder(ShaderOpcode.OP_LOAD)
                        .putWord(vec4)
                        .putWord(temp)
                        .putWord(mul.getWord(2))
                        .build(),
                    ShaderOpcode.Builder(ShaderOpcode.OP_STORE)
                        .putWord(output.id)
                        .putWord(temp)
                        .build()
                )
            }

            return code
        }
    }

    class ColorMixin(
        override val fragLocation: Int,
        @JvmField
        val locationMapper: ShaderLocationManager
    ) : AlbedoDynamicBuffer.MixinInstance {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.VERTEX) {
                if (!key.program.location.equals("sodium", "blocks/block_layer_opaque")) {
                    code.addPassthroughOutput(
                        ShaderVariableType.FLOAT_VEC2.findOrInjectBytecode(code),
                        (code.findVariable(name = key.program.format.getElementName(NeoVertexFormat.Element.TEXTURE_UV)) ?: return code).id,
                        AlbedoDynamicBuffer.VERTEX_TEX_COORD_VAR_NAME,
                        locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!
                            .map(1, AlbedoDynamicBuffer.VERTEX_TEX_COORD_VAR_NAME, 0)
                    )
                    code.addPassthroughOutput(
                        ShaderVariableType.FLOAT_VEC4.findOrInjectBytecode(code),
                        (code.findVariable(name = key.program.format.getElementName(NeoVertexFormat.Element.COLOR)) ?: return code).id,
                        AlbedoDynamicBuffer.VERTEX_COLOR_VAR_NAME,
                        locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, AlbedoDynamicBuffer.VERTEX_COLOR_VAR_NAME, 0)
                    )
                }
            } else if (key.type == ShaderSourceType.FRAGMENT) {
                val samplerVar = code.findVariable(name = "Sampler0") ?: return code

                val vec4 = ShaderVariableType.FLOAT_VEC4.findOrInjectBytecode(code)
                val vec2 = ShaderVariableType.FLOAT_VEC2.findOrInjectBytecode(code)

                val texCoord = if (key.program.location.equals("sodium", "blocks/block_layer_opaque")) {
                    code.findVariable(name = "v_TexCoord") ?: return code
                } else {
                    val texCoordLocation = locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.get(AlbedoDynamicBuffer.VERTEX_TEX_COORD_VAR_NAME) ?: return code
                    val texCoord = code.addStaticVar(ShaderStorageClass.INPUT, vec2, AlbedoDynamicBuffer.VERTEX_TEX_COORD_VAR_NAME)
                    code.setVariableLocation(texCoord.id, texCoordLocation)
                    texCoord
                }
                val color = if (key.program.location.equals("sodium", "blocks/block_layer_opaque")) {
                    code.findVariable(name = "v_Color") ?: return code
                } else {
                    val colorLocation = locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.get(AlbedoDynamicBuffer.VERTEX_COLOR_VAR_NAME) ?: return code
                    val color = code.addStaticVar(ShaderStorageClass.INPUT, vec4, AlbedoDynamicBuffer.VERTEX_COLOR_VAR_NAME)
                    code.setVariableLocation(color.id, colorLocation)
                    color
                }

                val output = code.addStaticVar(ShaderStorageClass.OUTPUT, vec4, AlbedoDynamicBuffer.FRAGMENT_VAR_NAME)
                code.setVariableLocation(output.id, fragLocation)

                val tempSamplerVar = code.bound++
                val tempTexCoordVar = code.bound++
                val tempColorVar = code.bound++
                val tempPreResultVar = code.bound++
                val tempResultVar = code.bound++
                code.insert(
                    code.findOpcodeInMethod("main", ShaderOpcode.OP_RETURN)!!.index,
                    ShaderOpcode.Builder(ShaderOpcode.OP_LOAD)
                        .putWord(samplerVar.type)
                        .putWord(tempSamplerVar)
                        .putWord(samplerVar.id)
                        .build(),
                    ShaderOpcode.Builder(ShaderOpcode.OP_LOAD)
                        .putWord(color.type)
                        .putWord(tempColorVar)
                        .putWord(color.id)
                        .build(),
                    ShaderOpcode.Builder(ShaderOpcode.OP_LOAD)
                        .putWord(vec4)
                        .putWord(tempTexCoordVar)
                        .putWord(texCoord.id)
                        .build(),

                    ShaderOpcode.Builder(ShaderOpcode.OP_IMAGE_SAMPLE_IMPLICIT_LOD)
                        .putWord(vec4)
                        .putWord(tempPreResultVar)
                        .putWord(tempSamplerVar)
                        .putWord(tempTexCoordVar)
                        .build(),
                    ShaderOpcode.Builder(ShaderOpcode.OP_F_MUL)
                        .putWord(vec4)
                        .putWord(tempResultVar)
                        .putWord(tempPreResultVar)
                        .putWord(tempColorVar)
                        .build(),

                    ShaderOpcode.Builder(ShaderOpcode.OP_STORE)
                        .putWord(output.id)
                        .putWord(tempResultVar)
                        .build()
                )
            }

            return code
        }
    }

    class Mixin(
        override val fragLocation: Int,
        @JvmField
        val locationMapper: ShaderLocationManager
    ) : AlbedoDynamicBuffer.MixinInstance {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.VERTEX) {
                code.addPassthroughOutput(
                    ShaderVariableType.FLOAT_VEC2.findOrInjectBytecode(code),
                    (code.findVariable(name = key.program.format.getElementName(NeoVertexFormat.Element.TEXTURE_UV)) ?: return code).id,
                    AlbedoDynamicBuffer.VERTEX_TEX_COORD_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, AlbedoDynamicBuffer.VERTEX_TEX_COORD_VAR_NAME, 0)
                )
            } else if (key.type == ShaderSourceType.FRAGMENT) {
                val samplerVar = code.findVariable(name = "Sampler0") ?: return code

                val vec4 = ShaderVariableType.FLOAT_VEC4.findOrInjectBytecode(code)
                val vec2 = ShaderVariableType.FLOAT_VEC2.findOrInjectBytecode(code)

                val inputLocation = locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.get(AlbedoDynamicBuffer.VERTEX_TEX_COORD_VAR_NAME) ?: return code
                val input = code.addStaticVar(ShaderStorageClass.INPUT, vec2, AlbedoDynamicBuffer.VERTEX_TEX_COORD_VAR_NAME)
                code.setVariableLocation(input.id, inputLocation)

                val output = code.addStaticVar(ShaderStorageClass.OUTPUT, vec4, AlbedoDynamicBuffer.FRAGMENT_VAR_NAME)
                code.setVariableLocation(output.id, fragLocation)

                val tempSamplerVar = code.bound++
                val tempTexCoordVar = code.bound++
                val tempResultVar = code.bound++
                code.insert(
                    code.findOpcodeInMethod("main", ShaderOpcode.OP_RETURN)!!.index,
                    ShaderOpcode.Builder(ShaderOpcode.OP_LOAD)
                        .putWord(samplerVar.type)
                        .putWord(tempSamplerVar)
                        .putWord(samplerVar.id)
                        .build(),
                    ShaderOpcode.Builder(ShaderOpcode.OP_LOAD)
                        .putWord(vec4)
                        .putWord(tempTexCoordVar)
                        .putWord(input.id)
                        .build(),

                    ShaderOpcode.Builder(ShaderOpcode.OP_IMAGE_SAMPLE_IMPLICIT_LOD)
                        .putWord(vec4)
                        .putWord(tempResultVar)
                        .putWord(tempSamplerVar)
                        .putWord(tempTexCoordVar)
                        .build(),

                    ShaderOpcode.Builder(ShaderOpcode.OP_STORE)
                        .putWord(output.id)
                        .putWord(tempResultVar)
                        .build()
                )
            }

            return code
        }
    }
}