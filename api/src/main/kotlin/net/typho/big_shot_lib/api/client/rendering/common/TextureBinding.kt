package net.typho.big_shot_lib.api.client.rendering.common

import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource

@Deprecated("")
sealed interface TextureBinding : MaybeNamedResource {
    val texture: GpuTexture
    val blur: Boolean
    val mipmap: Boolean

    data class FromLocation @JvmOverloads constructor(
        override val location: Identifier,
        override val blur: Boolean = false,
        override val mipmap: Boolean = true
    ) : TextureBinding {
        override val texture: GpuTexture
            get() = /*GpuTexture[location] ?: */throw FileNotFoundException("Couldn't find texture $location")
    }

    data class FromSupplier @JvmOverloads constructor(
        private val supplier: () -> GpuTexture,
        override val blur: Boolean = false,
        override val mipmap: Boolean = true
    ) : TextureBinding {
        override val texture: GpuTexture
            get() = supplier()
        override val location: Identifier?
            get() = texture.let { if (it is MaybeNamedResource) it.location else null }
    }
}