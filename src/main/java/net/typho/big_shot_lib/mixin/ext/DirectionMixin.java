package net.typho.big_shot_lib.mixin.ext;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.typho.big_shot_lib.api.ext.DirectionExtension;
import net.typho.big_shot_lib.api.math.IVec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Direction.class)
public class DirectionMixin implements DirectionExtension {
    @Shadow
    @Final
    private Vec3i normal;

    @Unique
    @NotNull
    @Override
    public Integer getX() {
        return normal.getX();
    }

    @Unique
    @NotNull
    @Override
    public Integer getY() {
        return normal.getY();
    }

    @Unique
    @NotNull
    @Override
    public Integer getZ() {
        return normal.getZ();
    }

    @Override
    @NotNull
    public IVec3<Integer> copyWith(@NotNull Integer x, @NotNull Integer y, @NotNull Integer z) {
        return IVec3.of(x, y, z);
    }
}
