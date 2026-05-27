package net.typho.big_shot_lib.mixin.impl.panorama;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.typho.big_shot_lib.api.client.rendering.AdvancedLogoRenderer;
import net.typho.big_shot_lib.api.client.rendering.PanoramaRendererExtension;
import net.typho.big_shot_lib.api.client.rendering.util.MainMenuMode;
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(AccessibilityOnboardingScreen.class)
public abstract class AccessibilityOnboardingScreenMixin extends Screen {
    @Shadow
    @Final
    private LogoRenderer logoRenderer;

    protected AccessibilityOnboardingScreenMixin(Component title) {
        super(title);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(Options options, Runnable onClose, CallbackInfo ci) {
        ((AdvancedLogoRenderer) logoRenderer).setEnabled(true);
    }

    @WrapMethod(
            method = "renderPanorama"
    )
    private void renderPanorama(GuiGraphics guiGraphics, float partialTick, Operation<Void> original) {
        if (((AdvancedLogoRenderer) logoRenderer).getEnabled() && MainMenuMode.getSelected().panorama != null) {
            PanoramaRendererExtension extension = (PanoramaRendererExtension) PANORAMA;
            CubeMap old = extension.getCubeMap();
            extension.setCubeMap(MainMenuMode.getSelected().panorama);

            original.call(guiGraphics, partialTick);

            extension.setCubeMap(old);
        } else {
            original.call(guiGraphics, partialTick);
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        ((AdvancedLogoRenderer) logoRenderer).mouseMoved(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (((AdvancedLogoRenderer) logoRenderer).mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
