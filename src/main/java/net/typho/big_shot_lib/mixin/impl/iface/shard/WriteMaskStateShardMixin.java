package net.typho.big_shot_lib.mixin.impl.iface.shard;

import kotlin.Pair;
import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
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
@Mixin(RenderStateShard.WriteMaskStateShard.class)
public class WriteMaskStateShardMixin implements MutableExtension<Pair<Boolean, Boolean>> {
    @Unique
    private boolean big_shot_lib$warned = false;
    @Unique
    private Pair<Boolean, Boolean> big_shot_lib$mask = null;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(boolean color, boolean depth, CallbackInfo ci) {
        big_shot_lib$mask = new Pair<>(color, depth);
    }

    @Override
    public Pair<Boolean, Boolean> getBig_shot_lib$extension_value() {
        if (big_shot_lib$mask == null) {
            if (!big_shot_lib$warned) {
                big_shot_lib$warned = true;
                BigShotApi.LOGGER.warn("Write Mask State Shard {} does not have defined mask values (this should NEVER happen), defaulting to disabled", this);
            }
            return null;
        } else {
            return big_shot_lib$mask;
        }
    }

    @Override
    public void setBig_shot_lib$extension_value(Pair<Boolean, Boolean> mask) {
        big_shot_lib$mask = mask;
    }
}
