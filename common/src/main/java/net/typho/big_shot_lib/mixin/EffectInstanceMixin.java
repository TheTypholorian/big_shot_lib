package net.typho.big_shot_lib.mixin;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.typho.big_shot_lib.spirv.ShaderInstanceLocationsExtension;
import net.typho.big_shot_lib.spirv.ShaderLocationsInfo;
import net.typho.big_shot_lib.spirv.ShaderMixinManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;

@Mixin(EffectInstance.class)
@Implements(@Interface(iface = ShaderInstanceLocationsExtension.class, prefix = "big_shot_lib$"))
public class EffectInstanceMixin {
    @Shadow
    @Final
    private List<String> samplerNames;
    @Shadow
    @Final
    private Map<String, IntSupplier> samplerMap;
    @Shadow
    @Final
    private List<Integer> samplerLocations;
    @Shadow
    @Final
    private List<Integer> uniformLocations;
    @Shadow
    @Final
    private Map<String, Uniform> uniformMap;
    @Shadow
    @Final
    private List<Uniform> uniforms;
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Final
    private String name;
    @Unique
    private final ShaderLocationsInfo big_shot_lib$locations = ShaderMixinManager.enabled ? new ShaderLocationsInfo(false) : null;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/EffectInstance;getOrCreate(Lnet/minecraft/server/packs/resources/ResourceProvider;Lcom/mojang/blaze3d/shaders/Program$Type;Ljava/lang/String;)Lcom/mojang/blaze3d/shaders/EffectProgram;",
                    ordinal = 0
            )
    )
    private void setThreadLocal(ResourceProvider resourceProvider, String name, CallbackInfo ci) {
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.set(DefaultVertexFormat.BLIT_SCREEN);
            ShaderMixinManager.currentLocationsInfo.set(big_shot_lib$locations);
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
        if (ShaderMixinManager.enabled) {
            ShaderMixinManager.currentVertexFormat.remove();
            ShaderMixinManager.currentLocationsInfo.remove();
        }
    }

    @Inject(
            method = "updateLocations",
            at = @At("HEAD"),
            cancellable = true
    )
    private void updateLocations(CallbackInfo ci) {
        if (big_shot_lib$locations != null) {
            samplerMap.keySet().removeIf(name -> !big_shot_lib$locations.uniforms.map.containsKey(name));
            samplerLocations.clear();

            for (String sampler : samplerNames) {
                Integer location = big_shot_lib$locations.uniforms.get(sampler);

                if (location == null) {
                    LOGGER.warn("Shader {} could not find sampler named {} in the specified shader program.", name, sampler);
                    samplerMap.remove(sampler);
                } else {
                    samplerLocations.add(location);
                }
            }

            samplerNames.retainAll(samplerMap.keySet());

            uniformLocations.clear();
            uniformMap.clear();

            for (Uniform uniform : uniforms) {
                Integer location = big_shot_lib$locations.uniforms.get(uniform.getName());

                if (location == null) {
                    LOGGER.warn("Shader {} could not find uniform named {} in the specified shader program.", name, uniform.getName());
                } else {
                    uniformLocations.add(location);
                    uniform.setLocation(location);
                    uniformMap.put(uniform.getName(), uniform);
                }
            }

            ci.cancel();
        }
    }

    public @Nullable ShaderLocationsInfo big_shot_lib$getLocations() {
        return big_shot_lib$locations;
    }
}
