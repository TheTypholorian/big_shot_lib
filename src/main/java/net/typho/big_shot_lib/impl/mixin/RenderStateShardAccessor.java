package net.typho.big_shot_lib.impl.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(RenderStateShard.class)
public interface RenderStateShardAccessor {
    @Accessor("name")
    String big_shot_lib$getName();
}
