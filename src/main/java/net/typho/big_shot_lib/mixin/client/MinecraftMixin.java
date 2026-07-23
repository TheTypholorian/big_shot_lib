package net.typho.big_shot_lib.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.typho.big_shot_lib.client.impl.NeoClientEventBusImpl;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Nullable
    public ClientLevel level;

    @Inject(
            method = "setLevel",
            at = @At("HEAD")
    )
    private void setLevel(ClientLevel level, CallbackInfo ci) {
        //NeoClientEventBusImpl.CLIENT_LEVEL_CHANGED.forEach(event -> event.onClientLevelChanged(this.level, level));
    }
}
