package net.typho.big_shot_lib.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.typho.big_shot_lib.impl.client.util.BigShotClientEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Nullable
    public ClientLevel level;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/client/resources/ClientPackSource"
            )
    )
    private void init(GameConfig p_91084_, CallbackInfo ci) {
        BigShotClientEvents.init$big_shot_lib();
    }

    @Inject(
            method = "setLevel",
            at = @At("HEAD")
    )
    private void setLevel(CallbackInfo ci, @Local(argsOnly = true) ClientLevel newLevel) {
        BigShotClientEvents.INSTANCE.getLevelChanged().forEach(event -> event.invoke(level, newLevel));
    }
}
