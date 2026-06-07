package net.typho.big_shot_lib.mixin.impl.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.typho.eye_spy.EyeSpy;
import net.typho.eye_spy.SpyglassData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "renderSpyglassOverlay",
            at = @At("TAIL")
    )
    private void renderSpyglassOverlay(
            GuiGraphics guiGraphics,
            float scopeScale,
            CallbackInfo ci,
            @Local ItemStack spyglass
    ) {
        SpyglassData data = spyglass.get(EyeSpy.spyglassDataComponent.get());

        if (data != null) {
            for (ItemStack attachment : data.attachments) {
                if (attachment.is(EyeSpy.rangeFinder.get())) {
                    Entity entity = minecraft.cameraEntity == null ? minecraft.player : minecraft.cameraEntity;

                    if (entity == null) {
                        return;
                    }

                    int maxRange = 1024;
                    HitResult hit = ((GameRendererAccessor) minecraft.gameRenderer).eye_spy$pick(entity, maxRange, maxRange, minecraft.getTimer().getGameTimeDeltaPartialTick(false));

                    Component text = hit.getType() == HitResult.Type.MISS ? Component.translatable("eye_spy.range_finder.out_of_range", maxRange) : Component.translatable("eye_spy.range_finder.range", (int) Math.sqrt(hit.distanceTo(entity)));

                    guiGraphics.drawCenteredString(minecraft.font, text, guiGraphics.guiWidth() / 2, guiGraphics.guiHeight() / 2 + 10, 0xFFFFFFFF);
                }
            }
        }
    }
}
