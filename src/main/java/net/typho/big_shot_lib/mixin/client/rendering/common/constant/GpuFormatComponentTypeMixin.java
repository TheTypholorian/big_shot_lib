package net.typho.big_shot_lib.mixin.client.rendering.common.constant;

import com.mojang.blaze3d.GpuFormat;
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuDataType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GpuFormat.ComponentType.class)
public class GpuFormatComponentTypeMixin implements GpuDataType {
}
