package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotLib;
import net.typho.big_shot_lib.api.client.rendering.state.TextureBinding;
import net.typho.big_shot_lib.api.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.EmptyTextureStateShard.class)
public class EmptyTextureStateShardMixin implements MutableExtension<TextureBinding> {
    @Unique
    private TextureBinding texture = null;

    @Override
    public TextureBinding getExtensionValue() {
        return texture;
    }

    @Override
    public void setExtensionValue(TextureBinding textureBinding) {
        texture = textureBinding;
    }
}
