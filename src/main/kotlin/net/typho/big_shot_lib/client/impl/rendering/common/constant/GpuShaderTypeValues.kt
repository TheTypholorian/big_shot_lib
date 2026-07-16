package net.typho.big_shot_lib.client.impl.rendering.common.constant

import com.mojang.blaze3d.shaders.ShaderType
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuShaderType
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

object GpuShaderTypeValues : GpuShaderType.Values {
    override val codec: Codec<GpuShaderType> = NeoCodecs.enumCodec<ShaderType>().xmap({ it }, { it.castTo() })
    override val vertex: GpuShaderType = ShaderType.VERTEX
    override val fragment: GpuShaderType = ShaderType.FRAGMENT
}