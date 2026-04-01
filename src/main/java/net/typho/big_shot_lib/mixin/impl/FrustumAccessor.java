package net.typho.big_shot_lib.mixin.impl;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Frustum.class)
public interface FrustumAccessor {
    @Accessor("intersection")
    FrustumIntersection big_shot_lib$getFrustmIntersection();
}
