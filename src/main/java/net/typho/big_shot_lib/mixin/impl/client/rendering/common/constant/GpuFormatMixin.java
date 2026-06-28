package net.typho.big_shot_lib.mixin.impl.client.rendering.common.constant;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.shaders.ShaderType;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuShaderType;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureFormat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GpuFormat.class)
public class GpuFormatMixin implements GpuTextureFormat {
}
