package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlOverlayShard;
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
@Mixin(RenderStateShard.OverlayStateShard.class)
public class OverlayStateShardMixin implements MutableExtension<GlOverlayShard> {
    @Unique
    private boolean big_shot_lib$warned = false;
    @Unique
    private GlOverlayShard big_shot_lib$overlay = null;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(boolean p_110238_, CallbackInfo ci) {
        big_shot_lib$overlay = new GlOverlayShard(p_110238_);
    }

    @Override
    public GlOverlayShard getBig_shot_lib$extension_value() {
        if (big_shot_lib$overlay == null) {
            if (!big_shot_lib$warned) {
                big_shot_lib$warned = true;
                BigShotApi.LOGGER.warn("Overlay State Shard {} does not have a defined GlOverlayShard value (this should NEVER happen), defaulting to disabled", this);
            }
            return null;
        } else {
            return big_shot_lib$overlay;
        }
    }

    @Override
    public void setBig_shot_lib$extension_value(GlOverlayShard glOverlayShard) {
        big_shot_lib$overlay = glOverlayShard;
    }
}
