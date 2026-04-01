package net.typho.big_shot_lib.mixin.impl;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
    @Accessor("level")
    ClientLevel big_shot_lib$getLevel();
}
