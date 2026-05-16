package net.typho.big_shot_lib.mixin.impl.iface;

import kotlin.Pair;
import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlColorMaskShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask;
import net.typho.big_shot_lib.impl.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.WriteMaskStateShard.class)
public class WriteMaskStateShardMixin implements MutableExtension<Pair<GlColorMaskShard, Boolean>> {
    @Unique
    private boolean big_shot_lib$warned = false;
    @Unique
    private Pair<GlColorMaskShard, Boolean> big_shot_lib$masks = null;

    @Override
    public Pair<GlColorMaskShard, Boolean> getBig_shot_lib$extension_value() {
        if (big_shot_lib$masks == null) {
            if (!big_shot_lib$warned) {
                big_shot_lib$warned = true;
                BigShotApi.LOGGER.warn("Write Mask State Shard {} does not have defined masks, defaulting to disabled", this);
            }
            return new Pair<>(new GlColorMaskShard(ColorMask.DISABLED), false);
        } else {
            return big_shot_lib$masks;
        }
    }

    @Override
    public void setBig_shot_lib$extension_value(Pair<GlColorMaskShard, Boolean> masks) {
        big_shot_lib$masks = masks;
    }
}
