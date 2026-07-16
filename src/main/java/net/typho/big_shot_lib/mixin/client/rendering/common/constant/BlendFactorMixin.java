package net.typho.big_shot_lib.mixin.client.rendering.common.constant;

import com.mojang.blaze3d.platform.BlendFactor;
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuBlendFactor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlendFactor.class)
public class BlendFactorMixin implements GpuBlendFactor {
}
