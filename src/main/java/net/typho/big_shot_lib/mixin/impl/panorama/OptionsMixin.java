package net.typho.big_shot_lib.mixin.impl.panorama;

import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Options.class)
public class OptionsMixin {
    // TODO
    /*
    @Inject(
            method = "processOptionsForge",
            at = @At("TAIL")
    )
    private void processOptionsForge(Options.FieldAccess accessor, CallbackInfo ci) {
        MainMenuModeManager.setSelected(Identifier.bySeparator(accessor.process("bigShotLibCurrentMainMenuMode", MainMenuModeManager.getSelected().id.toLanguageKey()), '.'));
    }
     */
}
