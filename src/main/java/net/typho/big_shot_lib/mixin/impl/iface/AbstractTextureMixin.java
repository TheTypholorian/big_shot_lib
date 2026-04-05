package net.typho.big_shot_lib.mixin.impl.iface;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//? if <1.21.5 {
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlTexture2D;
//? } else {
/*import com.mojang.blaze3d.textures.GpuTexture;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.jetbrains.annotations.Nullable;
*///? }

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(AbstractTexture.class)
public class AbstractTextureMixin implements ImmutableExtension<GlTexture2D> {
    //? if <1.21.5 {
    @Shadow
    protected int id;
    //? } else {
    /*@Shadow
    @Nullable
    protected GpuTexture texture;
    *///? }

    @Override
    public GlTexture2D getBig_shot_lib$extension_value() {
        //? if <1.21.5 {
        return id == -1 ? null : NeoGlTexture2D.ofExisting(id, GlTextureTarget.TEXTURE_2D);
        //? } else {
        /*return texture == null ? null : ImmutableExtensionKt.getExtensionValue(texture);
        *///? }
    }
}
