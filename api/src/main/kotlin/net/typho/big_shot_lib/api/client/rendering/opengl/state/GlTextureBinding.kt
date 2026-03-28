package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlSampler
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import java.io.FileNotFoundException

interface GlTextureBinding : MaybeNamedResource {
    val texture: GlTexture2D
    val target: GlTextureTarget
    val sampler: GlSampler?
    val uniformName: String?

    data class FromLocation @JvmOverloads constructor(
        override val location: NeoIdentifier,
        override val target: GlTextureTarget,
        override val sampler: GlSampler? = null,
        override val uniformName: String? = null
    ) : GlTextureBinding {
        override val texture: GlTexture2D
            get() = GlTexture2D[location] ?: throw FileNotFoundException("Couldn't find texture $location")
    }

    data class FromInstance @JvmOverloads constructor(
        override val texture: GlTexture2D,
        override val target: GlTextureTarget,
        override val sampler: GlSampler? = null,
        override val uniformName: String? = null
    ) : GlTextureBinding {
        override val location: NeoIdentifier? = if (texture is MaybeNamedResource) texture.location else null
    }
}