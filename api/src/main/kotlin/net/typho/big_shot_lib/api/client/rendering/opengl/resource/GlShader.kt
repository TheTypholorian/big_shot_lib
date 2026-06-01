package net.typho.big_shot_lib.api.client.rendering.opengl.resource

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.GlQueue
import net.typho.big_shot_lib.api.error.ShaderCompileException
import net.typho.big_shot_lib.api.util.resource.NamedResource
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL20.GL_COMPILE_STATUS
import org.lwjgl.opengl.GL20.glCompileShader
import org.lwjgl.opengl.GL20.glGetShaderInfoLog
import org.lwjgl.opengl.GL20.glGetShaderSource
import org.lwjgl.opengl.GL20.glGetShaderi
import org.lwjgl.opengl.GL20.glShaderSource

interface GlShader : NamedResource, GlResource {
    override val type: GlResourceType
        get() = shaderType.resourceType
    val shaderType: GlShaderType
    var source: String

    fun getInfoLog(): String

    fun compile(): Boolean

    fun compileOrThrow(onError: (log: String) -> Unit = { throw ShaderCompileException("Error compiling shader $location:\n$it") }) {
        if (!compile()) {
            onError(getInfoLog())
        }
    }

    @ApiStatus.Internal
    class Impl(
        override val location: Identifier,
        override val shaderType: GlShaderType,
        override val glId: Int
    ) : GlResource, GlShader {
        override var source: String
            get() = glGetShaderSource(glId)
            set(value) = glShaderSource(glId, value)
        override var freed: Boolean = false
            private set

        override fun close() {
            if (!freed) {
                freed = true
                GlQueue.INSTANCE.runOrQueue { type.destroy(glId) }
            }
        }

        override fun getInfoLog(): String {
            return glGetShaderInfoLog(glId, 4096).trim()
        }

        override fun compile(): Boolean {
            glCompileShader(glId)

            return glGetShaderi(glId, GL_COMPILE_STATUS) == GL_TRUE
        }
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        operator fun invoke(location: Identifier, type: GlShaderType, glId: Int = type.resourceType.create()) = InternalUtil.INSTANCE.createShader(
            location,
            type,
            glId,
        )
    }
}