package net.typho.big_shot_lib.mixin.ext;

import net.minecraft.core.BlockPos;
import net.typho.big_shot_lib.api.ext.MutableBlockPosExtension;
import net.typho.big_shot_lib.api.math.IVec2;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockPos.MutableBlockPos.class)
public abstract class MutableBlockPosMixin implements MutableBlockPosExtension {
    @Override
    public void setXy(@NotNull IVec2<@NotNull Integer> xy) {
        setX(xy.getX());
        setY(xy.getY());
    }

    @Override
    public void setYz(@NotNull IVec2<@NotNull Integer> yz) {
        setY(yz.getX());
        setZ(yz.getY());
    }

    @Override
    public void setXz(@NotNull IVec2<@NotNull Integer> xz) {
        setX(xz.getX());
        setZ(xz.getY());
    }
}
