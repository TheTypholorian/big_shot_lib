package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.realmsclient.client.RealmsClient;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint;
import net.typho.big_shot_lib.impl.client.util.BigShotClientEvents;
import net.typho.big_shot_lib.impl.client.util.InitialScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Shadow
    @Nullable
    public Screen screen;

    @Inject(
            method = "setLevel",
            at = @At("HEAD")
    )
    private void setLevel(CallbackInfo ci, @Local(argsOnly = true) ClientLevel newLevel) {
        BigShotClientEvents.INSTANCE.getLevelChanged().forEach(event -> event.invoke(level, newLevel));
    }

    //? if <=1.20 {
    @Inject(
            method = "setInitialScreen",
            at = @At("TAIL")
    )
    private void addInitialScreens(RealmsClient realmsClient, ReloadInstance reloadInstance, GameConfig.QuickPlayData quickPlayData, CallbackInfo ci) {
        List<Pair<Component, Function0<Unit>>> screens = new ArrayList<>();

        BigShotClientEntrypoint.Companion.displayInitialScreens((text, onClose) -> {
            screens.add(new Pair<>(text, onClose));
        });

        for (Pair<Component, Function0<Unit>> screen : CollectionsKt.reversed(screens)) {
            Screen nextScreen = this.screen;
            setScreen(new InitialScreen(screen.getFirst(), () -> {
                screen.getSecond().invoke();
                setScreen(nextScreen);
                return Unit.INSTANCE;
            }, new LogoRenderer(true)));
        }
    }
    //? } else {
    /*@Inject(
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
    *///? }
}
