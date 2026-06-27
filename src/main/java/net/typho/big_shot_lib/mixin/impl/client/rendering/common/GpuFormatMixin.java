package net.typho.big_shot_lib.mixin.impl.client.rendering.common;

import com.mojang.blaze3d.GpuFormat;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureFormat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GpuFormat.class)
public class GpuFormatMixin implements GpuTextureFormat {
}
