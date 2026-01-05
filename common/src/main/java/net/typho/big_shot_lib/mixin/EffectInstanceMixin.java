package net.typho.big_shot_lib.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo;
import net.typho.big_shot_lib.spirv.ShaderMixinCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectInstance.class)
public class EffectInstanceMixin {
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/EffectInstance;getOrCreate(Lnet/minecraft/server/packs/resources/ResourceProvider;Lcom/mojang/blaze3d/shaders/Program$Type;Ljava/lang/String;)Lcom/mojang/blaze3d/shaders/EffectProgram;",
                    ordinal = 0
            )
    )
    private void setThreadLocal(ResourceProvider resourceProvider, String name, CallbackInfo ci) {
        if (ShaderMixinCallback.enabled) {
            ShaderMixinCallback.currentVertexFormat.set(DefaultVertexFormat.BLIT_SCREEN);
            ShaderMixinCallback.currentLocationsInfo.set(new ShaderLocationsInfo(false));
        }
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/shaders/ProgramManager;createProgram()I"
            )
    )
    private void clearThreadLocal(ResourceProvider resourceProvider, String name, CallbackInfo ci) {
        if (ShaderMixinCallback.enabled) {
            ShaderMixinCallback.currentVertexFormat.remove();
            ShaderMixinCallback.currentLocationsInfo.remove();
        }
    }
}
