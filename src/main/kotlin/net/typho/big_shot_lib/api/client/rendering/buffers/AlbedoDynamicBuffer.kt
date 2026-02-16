package net.typho.big_shot_lib.api.client.rendering.buffers

import com.mojang.blaze3d.vertex.VertexFormatElement
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.*
import net.typho.big_shot_lib.api.client.rendering.shaders.variables.ShaderVariableType
import net.typho.big_shot_lib.api.client.rendering.textures.NeoTexture2D
import net.typho.big_shot_lib.api.client.rendering.textures.TextureFormat
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import java.util.function.Consumer

object AlbedoDynamicBuffer : DynamicBuffer {
    private var location: Int? = null
    val texture by lazy {
        NeoTexture2D(format())
    }
    const val VERTEX_TEX_COORD_VAR_NAME = "BigShotVertexTexCoord"
    const val VERTEX_COLOR_VAR_NAME = "BigShotVertexTexCoord"
    const val FRAGMENT_VAR_NAME = "BigShotFragmentAlbedo"

    override fun location(): ResourceIdentifier {
        return BigShotApi.id("dynamic_buffer/albedo")
    }

    override fun blend(): Boolean {
        return false
    }

    override fun format(): TextureFormat {
        return TextureFormat.RGBA8
    }

    override fun attachToFramebuffer(attachment: Int) {
        texture.attachToFramebuffer(attachment)
        location = attachment - GL_COLOR_ATTACHMENT0
    }

    override fun resize(
        width: Int,
        height: Int,
        upload: Consumer<BufferUploader>
    ) {
        texture.resize(width, height, upload)
    }

    override fun create(
        key: ShaderProgramKey,
        parent: ShaderMixinManager.Instance
    ): ShaderMixin? {
        if (key.disabledDynamicBuffers.contains(location())) {
            return null
        }

        if (location == null) {
            BigShotApi.LOGGER.warn("Location for ${location()} is null when compiling $key, skipping")
            return null
        }

        if (key.builtinDynamicBuffers.contains(location())) {
            return BuiltinMixin(location!!)
        }

        if (!key.format.contains(VertexFormatElement.UV0)) {
            return null
        }

        if (key.sources.contains(ShaderSourceType.GEOMETRY)) {
            BigShotApi.LOGGER.warn("${location()} currently doesn't support geometry shaders, skipping $key")
            return null
        }

        if (key.format.contains(VertexFormatElement.COLOR)) {
            return ColorMixin(location!!, (parent.getOrCreateMixinInstance(ShaderLocationMapperMixin) as ShaderLocationMapperMixin.Instance).locations)
        }

        return Mixin(location!!, (parent.getOrCreateMixinInstance(ShaderLocationMapperMixin) as ShaderLocationMapperMixin.Instance).locations)
    }

    class BuiltinMixin(
        @JvmField
        val fragLocation: Int
    ) : ShaderMixin {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.FRAGMENT) {
                code.findVariable(name = FRAGMENT_VAR_NAME)?.let {
                    code.findOpcode(ShaderOpcode.OP_DECORATE, 0 to it.id, 1 to 30)?.putWord(2, fragLocation)
                }
            }

            return code
        }
    }

    class ColorMixin(
        @JvmField
        val fragLocation: Int,
        @JvmField
        val locationMapper: ShaderLocationManager
    ) : ShaderMixin {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.VERTEX) {
                code.addPassthroughOutput(
                    ShaderVariableType.FLOAT_VEC2.findOrInjectBytecode(code),
                    code.findVariable(name = key.program.format.getElementName(VertexFormatElement.UV0))!!.id,
                    VERTEX_TEX_COORD_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, VERTEX_TEX_COORD_VAR_NAME)
                )
                code.addPassthroughOutput(
                    ShaderVariableType.FLOAT_VEC4.findOrInjectBytecode(code),
                    code.findVariable(name = key.program.format.getElementName(VertexFormatElement.COLOR))!!.id,
                    VERTEX_COLOR_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, VERTEX_COLOR_VAR_NAME)
                )
            } else if (key.type == ShaderSourceType.FRAGMENT) {
                val samplerVar = code.findVariable(name = if (key.program.location.equals("sodium", "blocks/block_layer_opaque")) "u_BlockTex" else "Sampler0")!!

                val vec4 = ShaderVariableType.FLOAT_VEC4.findOrInjectBytecode(code)
                val vec2 = ShaderVariableType.FLOAT_VEC2.findOrInjectBytecode(code)

                val texCoordLocation = locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.map(1, VERTEX_TEX_COORD_VAR_NAME)
                val texCoord = code.addStaticVar(ShaderStorageClass.INPUT, vec2, VERTEX_TEX_COORD_VAR_NAME)
                code.setVariableLocation(texCoord.id, texCoordLocation)

                val colorLocation = locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.map(1, VERTEX_COLOR_VAR_NAME)
                val color = code.addStaticVar(ShaderStorageClass.INPUT, vec4, VERTEX_COLOR_VAR_NAME)
                code.setVariableLocation(color.id, colorLocation)

                val output = code.addStaticVar(ShaderStorageClass.INPUT, vec2, FRAGMENT_VAR_NAME)
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
        @JvmField
        val fragLocation: Int,
        @JvmField
        val locationMapper: ShaderLocationManager
    ) : ShaderMixin {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.VERTEX) {
                code.addPassthroughOutput(
                    ShaderVariableType.FLOAT_VEC2.findOrInjectBytecode(code),
                    code.findVariable(name = key.program.format.getElementName(VertexFormatElement.UV0))!!.id,
                    VERTEX_TEX_COORD_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, VERTEX_TEX_COORD_VAR_NAME)
                )
            } else if (key.type == ShaderSourceType.FRAGMENT) {
                val samplerVar = code.findVariable(name = if (key.program.location.equals("sodium", "blocks/block_layer_opaque")) "u_BlockTex" else "Sampler0")!!

                val vec4 = ShaderVariableType.FLOAT_VEC4.findOrInjectBytecode(code)
                val vec2 = ShaderVariableType.FLOAT_VEC2.findOrInjectBytecode(code)

                val inputLocation = locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.map(1, VERTEX_TEX_COORD_VAR_NAME)
                val input = code.addStaticVar(ShaderStorageClass.INPUT, vec2, VERTEX_TEX_COORD_VAR_NAME)
                code.setVariableLocation(input.id, inputLocation)

                val output = code.addStaticVar(ShaderStorageClass.OUTPUT, vec4, FRAGMENT_VAR_NAME)
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