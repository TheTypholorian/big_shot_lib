package net.typho.big_shot_lib.mixin.util;

import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(CubeMap.class)
public class CubeMapMixin {
    @Shadow
    @Final
    @Mutable
    private List<ResourceLocation> sides;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(ResourceLocation baseImageLocation, CallbackInfo ci) {
        sides = new ArrayList<>(sides);
    }
}
