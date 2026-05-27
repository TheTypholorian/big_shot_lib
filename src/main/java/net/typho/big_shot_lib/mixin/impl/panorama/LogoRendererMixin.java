package net.typho.big_shot_lib.mixin.impl.panorama;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.AdvancedLogoRenderer;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram;
import net.typho.big_shot_lib.api.client.rendering.util.MainMenuMode;
import net.typho.big_shot_lib.api.client.rendering.util.NeoGuiGraphics;
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint;
import net.typho.big_shot_lib.api.plugin.Namespace;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LogoRenderer.class)
public class LogoRendererMixin implements AdvancedLogoRenderer {
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private boolean enabled = false;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private boolean focused = false;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private ScreenRectangle rect;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private double mouseX;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private double mouseY;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private int menuModeIndex = 0;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private MainMenuMode menuMode = BigShotClientEntrypoint.getMainMenuModes().getFirst();

    @ModifyArg(
            method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/Identifier;IIFFIIII)V"
            )
    )
    private Identifier renderLogo(Identifier texture) {
        if (menuMode.logoImage != null) {
            return menuMode.logoImage;
        } else {
            return texture;
        }
    }

    @WrapOperation(
            method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/Identifier;IIFFIIII)V",
                    ordinal = 1
            )
    )
    private void renderLogo(
            GuiGraphics guiGraphics,
            Identifier texture,
            int x,
            int y,
            float uOffset,
            float vOffset,
            int width,
            int height,
            int textureWidth,
            int textureHeight,
            Operation<Void> original
    ) {
        if (enabled) {
            if (menuMode.editionImage != null) {
                texture = menuMode.editionImage;
            }

            if (rect != null && rect.containsPoint((int) mouseX, (int) mouseY)) {
                int extraX = textureWidth / 8;
                int extraY = textureHeight / 8;
                x -= extraX;
                y -= extraY;
                textureWidth += extraX * 2;
                textureHeight += extraY * 2;

                GlProgram shader = GlProgram.getOrThrow(BigShotApi.id("hovered_edition"));

                shader.setUniform("MousePos", uniform -> uniform.set((float) mouseX, (float) mouseY));
                shader.setUniform("GuiSize", uniform -> uniform.set((float) guiGraphics.guiWidth(), (float) guiGraphics.guiHeight()));
                shader.setUniform("DarkColor", uniform -> uniform.set(menuMode.editionHoverDarkColor));
                shader.setUniform("LightColor", uniform -> uniform.set(menuMode.editionHoverLightColor));

                int offset = 2;

                // TODO
                ((NeoGuiGraphics) guiGraphics).blitWithShader(shader, texture, x + offset, y + offset, uOffset, vOffset, textureWidth, textureHeight, textureWidth, textureHeight);
                ((NeoGuiGraphics) guiGraphics).blitWithShader(shader, texture, x + offset, y - offset, uOffset, vOffset, textureWidth, textureHeight, textureWidth, textureHeight);
                ((NeoGuiGraphics) guiGraphics).blitWithShader(shader, texture, x - offset, y - offset, uOffset, vOffset, textureWidth, textureHeight, textureWidth, textureHeight);
                ((NeoGuiGraphics) guiGraphics).blitWithShader(shader, texture, x - offset, y + offset, uOffset, vOffset, textureWidth, textureHeight, textureWidth, textureHeight);
            }

            rect = new ScreenRectangle(x, y, textureWidth, textureHeight);
        }

        original.call(guiGraphics, texture, x, y, uOffset, vOffset, textureWidth, textureHeight, textureWidth, textureHeight);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (enabled && rect != null && rect.containsPoint((int) mouseX, (int) mouseY)) {
            menuModeIndex = (menuModeIndex + 1) % BigShotClientEntrypoint.getMainMenuModes().size();
            menuMode = BigShotClientEntrypoint.getMainMenuModes().get(menuModeIndex);
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }

        return false;
    }

    @Override
    public boolean getEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean b) {
        if (BigShotClientEntrypoint.getMainMenuModes().size() > 1) {
            enabled = b;
        }
    }

    @Override
    public void setFocused(boolean b) {
        focused = b;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    @NotNull
    public MainMenuMode getMenuMode() {
        return menuMode;
    }

    @Override
    @NotNull
    public ScreenRectangle getRectangle() {
        return rect;
    }
}
