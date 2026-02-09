package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.mojang.blaze3d.shaders.Program;
import net.typho.big_shot_lib.impl.util.VoidMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Program.Type.class)
public class ProgramTypeMixin {
    @Shadow
    @Final
    @Mutable
    private Map<String, Program> programs;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(String name, int extension, String glType, String par4, int par5, CallbackInfo ci) {
        programs = new VoidMap<>();
    }
}
