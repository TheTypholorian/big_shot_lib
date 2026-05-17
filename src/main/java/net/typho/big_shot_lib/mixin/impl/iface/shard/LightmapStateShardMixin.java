package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlLightmapShard;
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
@Mixin(RenderStateShard.LightmapStateShard.class)
public class LightmapStateShardMixin implements MutableExtension<GlLightmapShard> {
    @Unique
    private boolean big_shot_lib$warned = false;
    @Unique
    private GlLightmapShard big_shot_lib$lightmap = null;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(boolean p_110238_, CallbackInfo ci) {
        big_shot_lib$lightmap = new GlLightmapShard(p_110238_);
    }

    @Override
    public GlLightmapShard getBig_shot_lib$extension_value() {
        if (big_shot_lib$lightmap == null) {
            if (!big_shot_lib$warned) {
                big_shot_lib$warned = true;
                BigShotApi.LOGGER.warn("Lightmap State Shard {} does not have a defined GlLightmapShard value (this should NEVER happen), defaulting to disabled", this);
            }
            return null;
        } else {
            return big_shot_lib$lightmap;
        }
    }

    @Override
    public void setBig_shot_lib$extension_value(GlLightmapShard glLightmapShard) {
        big_shot_lib$lightmap = glLightmapShard;
    }
}
