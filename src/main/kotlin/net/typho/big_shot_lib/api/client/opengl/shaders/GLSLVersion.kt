package net.typho.big_shot_lib.api.client.opengl.shaders

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.util.resources.NeoCodecs

data class GLSLVersion(
    @JvmField
    val version: Int,
    @JvmField
    val profile: Profile? = null
) {
    companion object {
        @JvmField
        val DEFAULT = GLSLVersion(150, null)
        @JvmField
        val CODEC: Codec<GLSLVersion> = Codec.either(
            Codec.INT.xmap(
                { GLSLVersion(it) },
                { it.version }
            ),
            RecordCodecBuilder.mapCodec<GLSLVersion> {
                it.group(
                    Codec.INT.fieldOf("version").forGetter { version -> version.version },
                    NeoCodecs.enumCodec<Profile>().fieldOf("profile").forGetter { version -> version.profile }
                ).apply(it, ::GLSLVersion)
            }.codec()
        ).xmap(
            { either -> either.map({ it }, { it }) },
            { version -> Either.right(version) }
        )
    }

    @JvmField
    val versionString = "#version $this"

    init {
        if (profile != null && version < 150) {
            throw IllegalStateException("Cannot specify glsl version profile for version less than 150 (specified $this)")
        }
    }

    override fun toString(): String {
        return if (profile == null) version.toString() else "$version ${profile.name.lowercase()}"
    }

    enum class Profile {
        CORE,
        COMPATIBILITY,
        ES
    }
}
