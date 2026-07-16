package net.typho.big_shot_lib.mixin.client.rendering.common.constant;

import com.mojang.blaze3d.shaders.ShaderType;
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuShaderType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShaderType.class)
public class ShaderTypeMixin implements GpuShaderType {
}
