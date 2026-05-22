package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource
import java.io.FileNotFoundException

sealed interface GlTextureBinding : MaybeNamedResource {
    val texture: GlTexture2D
    val blur: Boolean
    val mipmap: Boolean

    data class FromLocation @JvmOverloads constructor(
        override val location: Identifier,
        override val blur: Boolean = false,
        override val mipmap: Boolean = true
    ) : GlTextureBinding {
        override val texture: GlTexture2D
            get() = GlTexture2D[location] ?: throw FileNotFoundException("Couldn't find texture $location")
    }

    data class FromSupplier @JvmOverloads constructor(
        private val supplier: () -> GlTexture2D,
        override val blur: Boolean = false,
        override val mipmap: Boolean = true
    ) : GlTextureBinding {
        override val texture: GlTexture2D
            get() = supplier()
        override val location: Identifier?
            get() = texture.let { if (it is MaybeNamedResource) it.location else null }
    }
}