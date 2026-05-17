package net.typho.big_shot_lib.mixin.impl.test;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            method = "getRendertypeSolidShader",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getRendertypeSolidShader(CallbackInfoReturnable<ShaderInstance> cir) {
        cir.setReturnValue(ImmutableExtensionKt.getExtensionValue(Objects.requireNonNull(NeoShaderLoader.INSTANCE.get(BigShotApi.id("solid"))), ShaderInstance.class));
    }
}
