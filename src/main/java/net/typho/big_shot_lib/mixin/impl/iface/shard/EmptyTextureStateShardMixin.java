package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding;
import net.typho.big_shot_lib.api.plugin.Namespace;
import net.typho.big_shot_lib.impl.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.EmptyTextureStateShard.class)
public class EmptyTextureStateShardMixin implements MutableExtension<GlTextureBinding> {
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private GlTextureBinding texture = null;

    @Override
    public GlTextureBinding getExtensionValue() {
        return texture;
    }

    @Override
    public void setExtensionValue(GlTextureBinding glTextureBinding) {
        texture = glTextureBinding;
    }
}
