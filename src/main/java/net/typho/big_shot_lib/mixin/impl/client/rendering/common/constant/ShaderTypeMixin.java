package net.typho.big_shot_lib.mixin.impl.client.rendering.common.constant;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.shaders.ShaderType;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuIndexType;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuShaderType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShaderType.class)
public class ShaderTypeMixin implements GpuShaderType {
}
