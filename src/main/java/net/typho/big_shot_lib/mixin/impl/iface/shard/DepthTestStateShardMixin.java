package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction;
import net.typho.big_shot_lib.impl.util.MutableExtension;
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
    private boolean big_shot_lib$warned = false;
    @Unique
    private GlAlphaFunction big_shot_lib$depthFunction = null;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(String p_110246_, int p_110247_, CallbackInfo ci) {
        big_shot_lib$depthFunction = GlNamed.getEnum(GlAlphaFunction.class, p_110247_);
    }

    @Override
    public GlAlphaFunction getBig_shot_lib$extension_value() {
        if (big_shot_lib$depthFunction == null) {
            if (!big_shot_lib$warned) {
                big_shot_lib$warned = true;
                BigShotApi.LOGGER.warn("Depth Test State Shard {} does not have a defined GlAlphaFunction value (this should NEVER happen), defaulting to disabled", this);
            }
            return null;
        } else {
            return big_shot_lib$depthFunction;
        }
    }

    @Override
    public void setBig_shot_lib$extension_value(GlAlphaFunction glAlphaFunction) {
        big_shot_lib$depthFunction = glAlphaFunction;
    }
}
