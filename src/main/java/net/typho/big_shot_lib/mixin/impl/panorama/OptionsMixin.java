package net.typho.big_shot_lib.mixin.impl.panorama;

import net.minecraft.client.Options;
import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.client.rendering.util.MainMenuMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    @Inject(
            method = "processOptionsForge",
            at = @At("TAIL")
    )
    private void processOptionsForge(Options.FieldAccess accessor, CallbackInfo ci) {
        MainMenuMode.setSelected(Identifier.bySeparator(accessor.process("bigShotLibCurrentMainMenuMode", MainMenuMode.getSelected().id.toLanguageKey()), '.'));
    }
}
