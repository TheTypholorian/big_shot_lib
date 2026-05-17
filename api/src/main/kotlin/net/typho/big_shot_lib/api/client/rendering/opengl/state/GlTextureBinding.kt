package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlSampler
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource
import java.io.FileNotFoundException

sealed interface GlTextureBinding : MaybeNamedResource {
    val texture: GlTexture2D
    val target: GlTextureTarget
    val sampler: GlSampler?

    data class FromLocation @JvmOverloads constructor(
        override val location: Identifier,
        override val target: GlTextureTarget = GlTextureTarget.TEXTURE_2D,
        override val sampler: GlSampler? = null,
    ) : GlTextureBinding {
        override val texture: GlTexture2D
            get() = GlTexture2D[location] ?: throw FileNotFoundException("Couldn't find texture $location")
    }

    data class FromInstance @JvmOverloads constructor(
        private val getter: () -> GlTexture2D,
        override val target: GlTextureTarget = GlTextureTarget.TEXTURE_2D,
        override val sampler: GlSampler? = null
    ) : GlTextureBinding {
        override val texture: GlTexture2D
            get() = getter()
        override val location: Identifier? = texture.let { if (it is MaybeNamedResource) it.location else null }
    }
}