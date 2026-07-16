package net.typho.big_shot_lib.client.impl.rendering.common.constant

import com.mojang.blaze3d.platform.CompareOp
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuAlphaFunction
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

object GpuAlphaFunctionValues : GpuAlphaFunction.Values {
    override val codec: Codec<GpuAlphaFunction> = NeoCodecs.enumCodec<CompareOp>().xmap({ it }, { it.castTo() })
    override val never: GpuAlphaFunction = CompareOp.NEVER_PASS
    override val less: GpuAlphaFunction = CompareOp.LESS_THAN
    override val equal: GpuAlphaFunction = CompareOp.EQUAL
    override val lequal: GpuAlphaFunction = CompareOp.LESS_THAN_OR_EQUAL
    override val greater: GpuAlphaFunction = CompareOp.GREATER_THAN
    override val notEqual: GpuAlphaFunction = CompareOp.NOT_EQUAL
    override val gequal: GpuAlphaFunction = CompareOp.GREATER_THAN_OR_EQUAL
    override val always: GpuAlphaFunction = CompareOp.ALWAYS_PASS
}