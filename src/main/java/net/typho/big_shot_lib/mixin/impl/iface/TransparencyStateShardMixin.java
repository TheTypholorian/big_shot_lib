package net.typho.big_shot_lib.mixin.impl.iface;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlBlendShard;
import net.typho.big_shot_lib.impl.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
//? }
@Mixin(RenderStateShard.TransparencyStateShard.class)
public class TransparencyStateShardMixin implements MutableExtension<GlBlendShard> {
    @Unique
    private boolean big_shot_lib$warned = false;
    @Unique
    private GlBlendShard big_shot_lib$blend = null;

    @Override
    public GlBlendShard getBig_shot_lib$extension_value() {
        if (big_shot_lib$blend == null) {
            if (!big_shot_lib$warned) {
                big_shot_lib$warned = true;
                BigShotApi.LOGGER.warn("Transparency State Shard {} does not have a defined GlBlendShard value, defaulting to disabled", this);
            }
            return GlBlendShard.Disabled.INSTANCE;
        } else {
            return big_shot_lib$blend;
        }
    }

    @Override
    public void setBig_shot_lib$extension_value(GlBlendShard glBlendShard) {
        big_shot_lib$blend = glBlendShard;
    }
}
