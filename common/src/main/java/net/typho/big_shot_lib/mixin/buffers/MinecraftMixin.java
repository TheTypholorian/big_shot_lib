package net.typho.big_shot_lib.mixin.buffers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBufferRegistry;
import net.typho.big_shot_lib.impl.buffers.DynamicBufferRegistryImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;<init>(Lnet/minecraft/server/packs/PackType;)V"
            )
    )
    private void init(GameConfig gameConfig, CallbackInfo ci) {
        if (DynamicBufferRegistry.INSTANCE instanceof DynamicBufferRegistryImpl impl) {
            impl.init();
        }
    }
}
