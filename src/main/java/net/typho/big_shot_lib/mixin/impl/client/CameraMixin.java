package net.typho.big_shot_lib.mixin.impl.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.typho.eye_spy.EyeSpy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    public abstract boolean isDetached();

    @SuppressWarnings("ConstantValue")
    @ModifyReturnValue(
            method = "getFluidInCamera",
            at = @At("RETURN")
    )
    private FogType getFluidInCamera(FogType fog) {
        if (Minecraft.getInstance().gameRenderer.getMainCamera() == (Object) this && !isDetached() && Minecraft.getInstance().cameraEntity instanceof LivingEntity living) {
            var data = living.getUseItem().get(EyeSpy.spyglassDataComponent.get());

            if (data != null && data.lens.is(EyeSpy.dissipationLens.get())) {
                if (fog == FogType.LAVA || fog == FogType.WATER) {
                    fog = FogType.NONE;
                }
            }
        }

        return fog;
    }
}
