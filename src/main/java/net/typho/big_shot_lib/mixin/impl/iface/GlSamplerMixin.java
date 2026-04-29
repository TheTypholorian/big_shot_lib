package net.typho.big_shot_lib.mixin.impl.iface;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlSampler;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlSampler;
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if <1.21.11 {
import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
//? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(com.mojang.blaze3d.opengl.GlSampler.class)
public class GlSamplerMixin implements ImmutableExtension<GlSampler> {
    @Shadow
    @Final
    private int id;

    @Override
    public GlSampler getBig_shot_lib$extension_value() {
        return new NeoGlSampler(id, false, RenderingContext.MAIN);
    }
}
