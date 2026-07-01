package net.typho.big_shot_lib.mixin.impl.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.phys.Vec3;
import net.typho.big_shot_lib.api.ext.Vec3Extension;
import net.typho.big_shot_lib.api.math.IVec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(Vec3.class)
public abstract class Vec3Mixin implements Vec3Extension {
    @Shadow
    @Final
    private double x;

    @Shadow
    @Final
    private double y;

    @Shadow
    @Final
    private double z;

    @ModifyReturnValue(
            method = "equals",
            at = @At("RETURN")
    )
    private boolean equals(boolean original, @Local(argsOnly = true) Object o) {
        if (original) {
            return true;
        } else if (o instanceof IVec3<?> vec && !(o instanceof Vec3)) {
            return Objects.equals(vec.getX(), x) && Objects.equals(vec.getY(), y) && Objects.equals(vec.getZ(), z);
        }

        return false;
    }
}
