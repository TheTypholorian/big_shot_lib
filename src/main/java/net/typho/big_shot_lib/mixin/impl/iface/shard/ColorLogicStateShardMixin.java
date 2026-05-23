package net.typho.big_shot_lib.mixin.impl.iface.shard;

import net.minecraft.client.renderer.RenderStateShard;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlLogicOp;
import net.typho.big_shot_lib.api.plugin.Namespace;
import net.typho.big_shot_lib.impl.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >=1.21.5 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@Mixin(RenderStateShard.ColorLogicStateShard.class)
public class ColorLogicStateShardMixin implements MutableExtension<GlLogicOp> {
    @Unique
    @Namespace(BigShotApi.MOD_ID)
    private GlLogicOp logic = null;

    @Override
    public GlLogicOp getExtensionValue() {
        return logic;
    }

    @Override
    public void setExtensionValue(GlLogicOp glLogicOp) {
        logic = glLogicOp;
    }
}
