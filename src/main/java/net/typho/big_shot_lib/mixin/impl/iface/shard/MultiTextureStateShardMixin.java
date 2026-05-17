package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlCullShard;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding;
import net.typho.big_shot_lib.impl.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.MultiTextureStateShard.class)
public class MultiTextureStateShardMixin implements MutableExtension<GlTextureBinding[]> {
    @Unique
    private GlTextureBinding[] big_shot_lib$textures = null;
}
