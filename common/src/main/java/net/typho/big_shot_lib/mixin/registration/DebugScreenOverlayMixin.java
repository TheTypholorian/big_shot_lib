package net.typho.big_shot_lib.mixin.registration;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.typho.big_shot_lib.BigShotClientEventStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Consumer;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyReturnValue(
            method = "getGameInformation",
            at = @At("RETURN")
    )
    private List<String> getGameInformation(
            List<String> list
    ) {
        for (Pair<Boolean, Consumer<Consumer<String>>> info : BigShotClientEventStorage.debugScreenInfo) {
            if (!info.getFirst() || !minecraft.showOnlyReducedInfo()) {
                list.add("");
                info.getSecond().accept(list::add);
            }
        }

        return list;
    }
}
