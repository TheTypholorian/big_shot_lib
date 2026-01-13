package net.typho.big_shot_lib.mixin;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;
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

@Mixin(ShaderInstance.class)
@Implements(@Interface(iface = ShaderInstanceLocationsExtension.class, prefix = "locations$"))
public abstract class ShaderInstanceMixin {
    @Shadow
    @Final
    @Mutable
    private List<String> samplerNames;
    @Shadow
    @Final
    @Mutable
    private Map<String, IntSupplier> samplerMap;
    @Shadow
    @Final
    @Mutable
    private List<Integer> samplerLocations;
    @Shadow
    @Final
    @Mutable
    private List<Integer> uniformLocations;
    @Shadow
    @Final
    @Mutable
    private Map<String, Uniform> uniformMap;
    @Shadow
    @Final
    @Mutable
    private List<Uniform> uniforms;
    @Shadow
    @Final
    static Logger LOGGER;
    @Shadow
    @Final
    @Mutable
    private String name;

    @Unique
    private ShaderLocationsInfo big_shot_lib$locations = ShaderMixinManager.enabled ? new ShaderLocationsInfo(false) : null;

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

    public @Nullable ShaderLocationsInfo locations$getLocations() {
        return big_shot_lib$locations;
    }

    public void locations$setLocations(ShaderLocationsInfo locations) {
        big_shot_lib$locations = locations;
    }
}
