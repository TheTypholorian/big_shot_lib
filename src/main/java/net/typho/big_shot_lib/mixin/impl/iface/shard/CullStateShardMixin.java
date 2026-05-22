package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.plugin.Namespace;
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
@Mixin(RenderStateShard.CullStateShard.class)
public class CullStateShardMixin implements MutableExtension<Boolean> {
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private boolean cull = true;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(boolean p_110238_, CallbackInfo ci) {
        cull = p_110238_;
    }

    @Override
    public Boolean getExtensionValue() {
        return cull;
    }

    @Override
    public void setExtensionValue(Boolean aBoolean) {
        cull = aBoolean;
    }
}
