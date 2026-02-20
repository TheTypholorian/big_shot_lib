package net.typho.big_shot_lib.api.client.rendering.buffers

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.meshes.NeoVertexFormat
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

object NormalsDynamicBuffer : DynamicBuffer {
    private var location: Int? = null
    val texture by lazy {
        NeoTexture2D(format())
    }
    const val INPUT_VAR_NAME = "a_Normal"
    const val VERTEX_VAR_NAME = "BigShotVertexNormal"
    const val FRAGMENT_VAR_NAME = "BigShotFragmentNormal"

    override fun location(): ResourceIdentifier {
        return BigShotApi.id("dynamic_buffer/normals")
    }

    override fun blend(): Boolean {
        return false
    }

    override fun format(): TextureFormat {
        return TextureFormat.RGB16_SNORM
    }

    override fun attachToFramebuffer(attachment: Int) {
        texture.attachToFramebuffer(attachment)
        location = attachment - GL_COLOR_ATTACHMENT0
    }

    override fun setShaderLocation(location: Int) {
        NormalsDynamicBuffer.location = location
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

        if (key.location.equals("sodium", "blocks/block_layer_opaque")) {
            return SodiumMixin(location!!, (parent.getOrCreateMixinInstance(ShaderLocationMapperMixin) as ShaderLocationMapperMixin.Instance).locations)
        }

        if (key.builtinDynamicBuffers.contains(location())) {
            return BuiltinMixin(location!!)
        }

        if (!key.format.contains(NeoVertexFormat.Element.NORMAL)) {
            return null
        }

        if (key.sources.contains(ShaderSourceType.GEOMETRY)) {
            BigShotApi.LOGGER.warn("${location()} currently doesn't support geometry shaders, skipping $key")
            return null
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

    class SodiumMixin(
        @JvmField
        val fragLocation: Int,
        @JvmField
        val locationMapper: ShaderLocationManager
    ) : ShaderMixin {
        override fun mixinBytecode(key: ShaderSourceKey, code: ShaderBytecodeBuffer): ShaderBytecodeBuffer {
            if (key.type == ShaderSourceType.VERTEX) {
                code.addPassthroughInputOutput(
                    ShaderVariableType.FLOAT_VEC3.findOrInjectBytecode(code),
                    INPUT_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.map(1, INPUT_VAR_NAME),
                    VERTEX_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, VERTEX_VAR_NAME)
                )
            } else if (key.type == ShaderSourceType.FRAGMENT) {
                code.addPassthroughInputOutput(
                    ShaderVariableType.FLOAT_VEC3.findOrInjectBytecode(code),
                    VERTEX_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.get(VERTEX_VAR_NAME) ?: return code,
                    FRAGMENT_VAR_NAME,
                    fragLocation
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
                    ShaderVariableType.FLOAT_VEC3.findOrInjectBytecode(code),
                    (code.findVariable(name = key.program.format.getElementName(NeoVertexFormat.Element.NORMAL)) ?: return code).id,
                    VERTEX_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.OUTPUT, key.type)!!.map(1, VERTEX_VAR_NAME)
                )
            } else if (key.type == ShaderSourceType.FRAGMENT) {
                code.addPassthroughInputOutput(
                    ShaderVariableType.FLOAT_VEC3.findOrInjectBytecode(code),
                    VERTEX_VAR_NAME,
                    locationMapper.getMapper(ShaderStorageClass.INPUT, key.type)!!.get(VERTEX_VAR_NAME) ?: return code,
                    FRAGMENT_VAR_NAME,
                    fragLocation
                )
            }

            return code
        }
    }
}