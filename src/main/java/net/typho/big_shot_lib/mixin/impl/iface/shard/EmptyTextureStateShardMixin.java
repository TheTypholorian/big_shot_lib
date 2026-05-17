package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlCullShard;
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
@Mixin(RenderStateShard.EmptyTextureStateShard.class)
public class EmptyTextureStateShardMixin implements MutableExtension<GlCullShard> {
    @Unique
    private GlCullShard big_shot_lib$cull = null;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(boolean p_110238_, CallbackInfo ci) {
        big_shot_lib$cull = p_110238_ ? new GlCullShard.Enabled() : GlCullShard.Disabled.INSTANCE;
    }

    @Override
    public GlCullShard getBig_shot_lib$extension_value() {
        return big_shot_lib$cull;
    }

    @Override
    public void setBig_shot_lib$extension_value(GlCullShard glCullShard) {
        big_shot_lib$cull = glCullShard;
    }
}
