package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlLayeringShard;
import net.typho.big_shot_lib.impl.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.LayeringStateShard.class)
public class LayeringStateShardMixin implements MutableExtension<GlLayeringShard> {
    @Unique
    private boolean big_shot_lib$warned = false;
    @Unique
    private GlLayeringShard big_shot_lib$layering = null;

    @Override
    public GlLayeringShard getBig_shot_lib$extension_value() {
        if (big_shot_lib$layering == null) {
            if (!big_shot_lib$warned) {
                big_shot_lib$warned = true;
                BigShotApi.LOGGER.warn("Layering State Shard {} does not have a defined GlLayeringShard value, defaulting to disabled", this);
            }
            return GlLayeringShard.Disabled.INSTANCE;
        } else {
            return big_shot_lib$layering;
        }
    }

    @Override
    public void setBig_shot_lib$extension_value(GlLayeringShard glLayeringShard) {
        big_shot_lib$layering = glLayeringShard;
    }
}
