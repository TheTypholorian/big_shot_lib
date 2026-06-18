package net.typho.big_shot_lib.mixin.impl.ext;

import net.minecraft.world.phys.Vec3;
import net.typho.big_shot_lib.api.ext.Vec3Extension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Vec3.class)
public abstract class Vec3Mixin implements Vec3Extension {
    @Shadow
    @Final
    public double x;

    @Shadow
    @Final
    public double y;

    @Shadow
    @Final
    public double z;

    @NotNull
    @Override
    public Double getX() {
        return x;
    }

    @NotNull
    @Override
    public Double getY() {
        return y;
    }

    @NotNull
    @Override
    public Double getZ() {
        return z;
    }
}
