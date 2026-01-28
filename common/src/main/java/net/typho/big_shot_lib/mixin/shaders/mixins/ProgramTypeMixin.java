package net.typho.big_shot_lib.mixin.shaders.mixins;

import com.mojang.blaze3d.shaders.Program;
import net.typho.big_shot_lib.shaders.mixins.ShaderMixinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Program.Type.class)
public class ProgramTypeMixin {
    @Inject(
            method = "getPrograms",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getPrograms(CallbackInfoReturnable<Map<String, Program>> cir) {
        if (ShaderMixinManager.enabled) {
            cir.setReturnValue(new HashMap<>());
        }
    }
}
