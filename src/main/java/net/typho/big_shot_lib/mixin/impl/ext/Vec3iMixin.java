package net.typho.big_shot_lib.mixin.impl.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Vec3i;
import net.typho.big_shot_lib.api.ext.Vec3iExtension;
import net.typho.big_shot_lib.api.math.IVec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(Vec3i.class)
public abstract class Vec3iMixin implements Vec3iExtension {
    @Shadow
    private int x;

    @Shadow
    private int y;

    @Shadow
    private int z;

    @ModifyReturnValue(
            method = "equals",
            at = @At("RETURN")
    )
    private boolean equals(boolean original, @Local(argsOnly = true) Object o) {
        if (original) {
            return true;
        } else if (o instanceof IVec3<?> vec && !(o instanceof Vec3i)) {
            return Objects.equals(vec.getX(), x) && Objects.equals(vec.getY(), y) && Objects.equals(vec.getZ(), z);
        }

        return false;
    }
}
