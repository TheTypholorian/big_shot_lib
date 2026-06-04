package net.typho.big_shot_lib.mixin.impl.data.loot;

import com.google.gson.JsonElement;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.event.RegisterEvent;
import net.typho.big_shot_lib.impl.NeoEventBusImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ReloadableServerRegistries.class)
public class ReloadableServerRegistriesMixin {
    @Inject(
            method = "lambda$scheduleElementParse$4",
            at = @At("RETURN")
    )
    private static <T> void run(
            LootDataType<T> lootDataType,
            ResourceManager resourceManager,
            RegistryOps<JsonElement> registryOps,
            CallbackInfoReturnable<WritableRegistry<?>> cir
    ) {
        var out = new RegisterEvent.Output.ToRegistry<>(cir.getReturnValue());

        for (RegisterEvent event : NeoEventBusImpl.REGISTER_EVENTS) {
            event.register(out);
        }

        BigShotApi.LOGGER.info("Loaded {} dynamic loot tables", out.getCount());
    }
}
