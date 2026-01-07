package net.typho.big_shot_lib.api.peer

import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.server.packs.resources.ResourceProvider
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.api.IShader

@Suppress("DEPRECATION")
open class ShaderPeer(
    protected var shader: IShader,
    resourceProvider: ResourceProvider,
    name: String,
    vertexFormat: VertexFormat
) : ShaderInstance(resourceProvider, name, vertexFormat) {
    companion object {
        @JvmStatic
        fun create(shader: IShader): ShaderPeer {
            val peer = BigShotLib.UNSAFE.allocateInstance(ShaderPeer::class.java) as ShaderPeer
            val extension = peer as ShaderInstanceUnsafeExtension

            peer.shader = shader

            extension.setName(shader.location().toString())
            extension.setFormat(shader.vertexFormat()!!)
            extension.setProgramId(shader.id())
            extension.setVertex(ProgramWrapper(
                Program.Type.VERTEX,
                shader.location()!!.withSuffix(Program.Type.VERTEX.extension).toString()
            ))
            extension.setFragment(ProgramWrapper(
                Program.Type.FRAGMENT,
                shader.location()!!.withSuffix(Program.Type.FRAGMENT.extension).toString()
            ))
            extension.initialize()

            return peer
        }
    }

    override fun close() {
        shader.release()

        for (uniform in (this as ShaderInstanceUnsafeExtension).getUniforms()) {
            uniform.close()
        }
    }
}