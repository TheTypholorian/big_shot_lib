package net.typho.big_shot_lib.mixin.client.rendering.common.constant;

import com.mojang.blaze3d.IndexType;
import net.typho.big_shot_lib.client.api.rendering.common.constant.GpuIndexType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IndexType.class)
public class IndexTypeMixin implements GpuIndexType {
}
