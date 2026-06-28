package net.typho.big_shot_lib.mixin.impl.client.rendering.common.constant;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.platform.BlendFactor;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBlendFactor;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuDataType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GpuFormat.ComponentType.class)
public class GpuFormatComponentTypeMixin implements GpuDataType {
}
