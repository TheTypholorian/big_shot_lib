package net.typho.big_shot_lib.mixin.impl.client.rendering.common.constant;

import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuAlphaFunction;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBlendFactor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlendFactor.class)
public class BlendFactorMixin implements GpuBlendFactor {
}
