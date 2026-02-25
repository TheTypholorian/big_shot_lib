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
import net.typho.big_shot_lib.api.client.util.dynamic_buffers.NormalsDynamicBuffer
import net.typho.big_shot_lib.api.util.NeoCollections
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

class NormalsDynamicBufferImpl : NormalsDynamicBuffer.Impl {
    val builtin = NeoCollections.flatListOf<ResourceIdentifier>(
        RenderPipelines.getStaticPipelines()
            .filter { it.vertexShader.toNeo().equals("minecraft", "core/terrain") && it.fragmentShader.toNeo().equals("minecraft", "core/terrain") }
            .map { it.location.toNeo() },
        ResourceIdentifier("sodium", "blocks/block_layer_opaque")
    )

    override fun create(
        key: ShaderProgramKey,
        parent: ShaderMixinManager.Instance,
        location: Int?
    ): NormalsDynamicBuffer.MixinInstance? {
        if (key.disabledDynamicBuffers.contains(NormalsDynamicBuffer.location())) {
            return null
        }

        if (location == null) {
            BigShotApi.LOGGER.warn("Location for ${NormalsDynamicBuffer.location()} is null when compiling $key, skipping")
            return null
        }

        if (key.location.equals("sodium", "blocks/block_layer_opaque")) {
            return SodiumMixin(location, (parent.getOrCreateMixinInstance(ShaderLocationMapperMixin) as ShaderLocationMapperMixin.Instance).locations)
        }

        if (key.builtinDynamicBuffers.contains(NormalsDynamicBuffer.location()) || builtin.contains(key.location)) {
            return BuiltinMixin(location)
        }

        if (!key.format.contains(NeoVertexFormat.Element.NORMAL)) {
            return null
        }

        if (key.sources.contains(ShaderSourceType.GEOMETRY)) {
            BigShotApi.LOGGER.warn("${NormalsDynamicBuffer.location()} currently doesn't support geometry shaders, skipping $key")
            return null
        }

        return Mixin(location, (parent.getOrCreateMixinInstance(ShaderLocationMapperMixin) as ShaderLocationMapperMixin.Instance).locations)
    }

    class BuiltinMixin(
        override val fragLocation: Int
    ) : NormalsDynamicBuffer.MixinInstance {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.FRAGMENT) {
                code.findVariable(name = NormalsDynamicBuffer.FRAGMENT_VAR_NAME)?.let {
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
    ) : NormalsDynamicBuffer.MixinInstance {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.VERTEX) {
                code.addPassthroughInputOutput(
                    ShaderVariableType.FLOAT_VEC3.findOrInjectBytecode(code),
                    NormalsDynamicBuffer.INPUT_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.map(1, NormalsDynamicBuffer.INPUT_VAR_NAME),
                    NormalsDynamicBuffer.VERTEX_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, NormalsDynamicBuffer.VERTEX_VAR_NAME)
                )
            } else if (key.type == ShaderSourceType.FRAGMENT) {
                code.addPassthroughInputOutput(
                    ShaderVariableType.FLOAT_VEC3.findOrInjectBytecode(code),
                    NormalsDynamicBuffer.VERTEX_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.get(NormalsDynamicBuffer.VERTEX_VAR_NAME) ?: return code,
                    NormalsDynamicBuffer.FRAGMENT_VAR_NAME,
                    fragLocation
                )
            }

            return code
        }
    }

    class Mixin(
        override val fragLocation: Int,
        @JvmField
        val locationMapper: ShaderLocationManager
    ) : NormalsDynamicBuffer.MixinInstance {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.VERTEX) {
                code.addPassthroughOutput(
                    ShaderVariableType.FLOAT_VEC3.findOrInjectBytecode(code),
                    (code.findVariable(name = key.program.format.getElementName(NeoVertexFormat.Element.NORMAL)) ?: return code).id,
                    NormalsDynamicBuffer.VERTEX_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, NormalsDynamicBuffer.VERTEX_VAR_NAME)
                )
            } else if (key.type == ShaderSourceType.FRAGMENT) {
                code.addPassthroughInputOutput(
                    ShaderVariableType.FLOAT_VEC3.findOrInjectBytecode(code),
                    NormalsDynamicBuffer.VERTEX_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.get(NormalsDynamicBuffer.VERTEX_VAR_NAME) ?: return code,
                    NormalsDynamicBuffer.FRAGMENT_VAR_NAME,
                    fragLocation
                )
            }

            return code
        }
    }
}