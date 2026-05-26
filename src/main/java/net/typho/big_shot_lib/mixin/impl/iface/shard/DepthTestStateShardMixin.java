package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction;
import net.typho.big_shot_lib.api.plugin.Namespace;
import net.typho.big_shot_lib.api.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.DepthTestStateShard.class)
public class DepthTestStateShardMixin implements MutableExtension<GlAlphaFunction> {
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private boolean warned = false;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private GlAlphaFunction depthFunction = null;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(String p_110246_, int p_110247_, CallbackInfo ci) {
        depthFunction = GlNamed.getEnum(GlAlphaFunction.class, p_110247_);
    }

    @Override
    public GlAlphaFunction getExtensionValue() {
        if (depthFunction == null) {
            if (!warned) {
                warned = true;
                BigShotApi.LOGGER.warn("Depth Test State Shard {} does not have a defined GlAlphaFunction value (this should NEVER happen), defaulting to disabled", this);
            }
            return null;
        } else {
            return depthFunction;
        }
    }

    @Override
    public void setExtensionValue(GlAlphaFunction glAlphaFunction) {
        warned = true;
        depthFunction = glAlphaFunction;
    }
}
