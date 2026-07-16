package net.typho.big_shot_lib.mixin.ext;

import net.minecraft.core.BlockPos;
import net.typho.big_shot_lib.api.ext.BlockPosExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockPos.class)
public abstract class BlockPosMixin implements BlockPosExtension {
}
