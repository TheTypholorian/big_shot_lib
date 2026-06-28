package net.typho.big_shot_lib.mixin.impl.client.rendering.common.constant;

import com.mojang.blaze3d.platform.CompareOp;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuAlphaFunction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CompareOp.class)
public class CompareOpMixin implements GpuAlphaFunction {
}
