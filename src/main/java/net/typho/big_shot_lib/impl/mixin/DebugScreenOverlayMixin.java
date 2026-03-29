package net.typho.big_shot_lib.impl.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.typho.big_shot_lib.impl.client.util.BigShotClientEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

//? if >=1.21.9 {
import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
//? }
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
        for (Pair<Boolean, Function1<Function1<? super String, Unit>, Unit>> info : BigShotClientEvents.debugScreenInfo) {
            if (!info.getFirst() || !minecraft.showOnlyReducedInfo()) {
                list.add("");
                info.getSecond().invoke(s -> {
                    list.add(s);
                    return Unit.INSTANCE;
                });
            }
        }

        return list;
    }
}
