package net.typho.big_shot_lib.impl.mixin;

import net.minecraft.server.MinecraftServer;
import net.typho.big_shot_lib.impl.util.BigShotCommonEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
            method = "<init>",
            at = @At("CTOR_HEAD")
    )
    private void init(CallbackInfo ci) {
        BigShotCommonEvents.init$big_shot_lib();
    }
}
