package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.state.LayeringState;
import net.typho.big_shot_lib.api.plugin.Namespace;
import net.typho.big_shot_lib.api.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.LayeringStateShard.class)
public class LayeringStateShardMixin implements MutableExtension<LayeringState> {
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private boolean warned = false;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private LayeringState layering = null;

    @Override
    public LayeringState getExtensionValue() {
        if (layering == null) {
            if (!warned) {
                warned = true;
                BigShotApi.LOGGER.warn("Layering State Shard {} does not have a defined LayeringState value, defaulting to disabled", this);
            }
            return LayeringState.DISABLED;
        } else {
            return layering;
        }
    }

    @Override
    public void setExtensionValue(LayeringState layeringState) {
        warned = true;
        layering = layeringState;
    }
}
