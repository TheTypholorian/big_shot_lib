package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.shaders.ProgramManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProgramManager.class)
public class ProgramManagerMixin {
    @WrapOperation(
            method = "releaseProgram",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/shaders/Program;close()V"
            )
    )
    private static void releaseProgram(Program instance, Operation<Void> original) {
        if (instance != null) {
            original.call(instance);
        }
    }
}
