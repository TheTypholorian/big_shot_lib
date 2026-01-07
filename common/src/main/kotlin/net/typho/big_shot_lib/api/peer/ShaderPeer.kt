package net.typho.big_shot_lib.api.peer

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.server.packs.resources.ResourceProvider
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.api.IShader

open class ShaderPeer(
    protected var shader: IShader,
    resourceProvider: ResourceProvider,
    name: String,
    vertexFormat: VertexFormat
) : ShaderInstance(resourceProvider, name, vertexFormat) {
    companion object {
        @JvmStatic
        fun create(
            shader: IShader
        ): ShaderPeer {
            val peer = BigShotLib.UNSAFE.allocateInstance(ShaderPeer::class.java) as ShaderPeer
            val extension = peer as ShaderInstanceUnsafeExtension

            peer.shader = shader

            extension.setName(shader.location().toString())
            extension.setFormat(shader.vertexFormat()!!)
            extension.setProgramId(shader.id())
            extension.setVertex(ProgramWrapper(
                Program.Type.VERTEX,
                shader.location()!!.withSuffix("/vertex").toString()
            ))
            extension.setFragment(ProgramWrapper(
                Program.Type.FRAGMENT,
                shader.location()!!.withSuffix("/fragment").toString()
            ))
            extension.init()

            return peer
        }
    }

    override fun close() {
        shader.release()
    }

    override fun apply() {
        shader.bind()
    }

    override fun clear() {
        shader.unbind()
    }

    override fun setSampler(name: String, textureId: Any) {
        when (textureId) {
            is RenderTarget -> shader.setSampler(name, textureId)
            is AbstractTexture -> shader.setSampler(name, textureId)
            is Int -> shader.setSampler(name, textureId)
            else -> throw UnsupportedOperationException("Cannot set sampler $name to $textureId")
        }
    }
}