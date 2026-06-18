package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.function.Function;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Nullable
    public ClientLevel level;

    @Inject(
            method = "setLevel",
            at = @At("HEAD")
    )
    private void setLevel(CallbackInfo ci, @Local(argsOnly = true) ClientLevel newLevel) {
        // TODO
        //BigShotClientEvents.INSTANCE.getLevelChanged().forEach(event -> event.invoke(level, newLevel));
    }

    @Inject(
            method = "addInitialScreens",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;multiplayerBan()Lcom/mojang/authlib/minecraft/BanDetails;"
            )
    )
    private void addInitialScreens(List<Function<Runnable, Screen>> list, CallbackInfo ci) {
        // TODO
        /*
        NeoClientInitializer.Companion.displayInitialScreens((text, onClose) -> {
            list.add(onClose1 -> new InitialScreen(text, () -> {
                onClose.invoke();
                onClose1.run();
                return Unit.INSTANCE;
            }, new LogoRenderer(true)));
        });
         */
    }
}
