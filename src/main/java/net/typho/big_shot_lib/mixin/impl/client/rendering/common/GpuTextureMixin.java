package net.typho.big_shot_lib.mixin.impl.client.rendering.common;

import com.mojang.blaze3d.textures.GpuTexture;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResource;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResourceType;
import net.typho.big_shot_lib.api.client.rendering.common.Recyclable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GpuTexture.class)
public abstract class GpuTextureMixin implements GpuResource, Recyclable {
    @Shadow
    public abstract void close();

    @Override
    public @NotNull GpuResourceType getType() {
        return GpuResourceType.TEXTURE;
    }

    @Override
    public void recycle() {
        close();
    }
}
