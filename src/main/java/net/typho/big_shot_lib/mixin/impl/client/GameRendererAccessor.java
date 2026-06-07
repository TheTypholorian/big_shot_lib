package net.typho.big_shot_lib.mixin.impl.client;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Invoker("pick")
    HitResult eye_spy$pick(Entity entity, double blockInteractionRange, double entityInteractionRange, float partialTick);
}
