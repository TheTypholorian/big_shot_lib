package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction;
import net.typho.big_shot_lib.api.plugin.Namespace;
import net.typho.big_shot_lib.api.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.TransparencyStateShard.class)
public class TransparencyStateShardMixin implements MutableExtension<BlendFunction> {
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private boolean warned = false;
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private BlendFunction blend = null;

    @Override
    public BlendFunction getExtensionValue() {
        if (blend == null) {
            if (!warned) {
                warned = true;
                BigShotApi.LOGGER.warn("Transparency State Shard {} does not have a defined BlendFunction value, defaulting to null", this);
            }
            return null;
        } else {
            return blend;
        }
    }

    @Override
    public void setExtensionValue(BlendFunction blendFunction) {
        warned = true;
        blend = blendFunction;
    }
}
