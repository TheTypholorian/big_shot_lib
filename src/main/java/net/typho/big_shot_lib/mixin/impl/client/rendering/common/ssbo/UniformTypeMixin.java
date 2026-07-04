package net.typho.big_shot_lib.mixin.impl.client.rendering.common.ssbo;

import com.mojang.blaze3d.shaders.UniformType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(UniformType.class)
public enum UniformTypeMixin {
    BIG_SHOT_LIB_SHADER_STORAGE_BUFFER
}
