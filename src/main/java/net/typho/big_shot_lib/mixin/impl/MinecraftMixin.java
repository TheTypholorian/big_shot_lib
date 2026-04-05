package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint;
import net.typho.big_shot_lib.impl.client.util.BigShotClientEvents;
import net.typho.big_shot_lib.impl.client.util.InitialScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.function.Function;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
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
        BigShotClientEvents.INSTANCE.getLevelChanged().forEach(event -> event.invoke(level, newLevel));
    }

    @Inject(
            method = "addInitialScreens",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;multiplayerBan()Lcom/mojang/authlib/minecraft/BanDetails;"
            )
    )
    private void addInitialScreens(List<Function<Runnable, Screen>> list, CallbackInfo ci) {
        BigShotClientEntrypoint.Companion.displayInitialScreens((text, onClose) -> {
            list.add(onClose1 -> new InitialScreen(text, () -> {
                onClose.invoke();
                onClose1.run();
                return Unit.INSTANCE;
            }, new LogoRenderer(true)));
        });
    }
}
