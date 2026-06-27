package net.typho.big_shot_lib.mixin.impl.client.rendering.common;

import com.mojang.blaze3d.textures.GpuTextureImpl;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResource;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResourceType;
import net.typho.big_shot_lib.api.client.rendering.common.GpuTexture;
import net.typho.big_shot_lib.api.client.rendering.common.Recyclable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GpuTextureImpl.class)
public abstract class GpuTextureImplMixin implements GpuTexture, GpuResource, Recyclable {
    @Shadow
    public abstract void close();

    @Shadow
    public abstract int getWidth(int mipLevel);

    @Shadow
    public abstract int getHeight(int mipLevel);

    @Override
    public @NotNull GpuResourceType getType() {
        return GpuResourceType.TEXTURE;
    }

    @Override
    public void recycle() {
        close();
    }

    @Override
    public int getWidth() {
        return getWidth(0);
    }

    @Override
    public int getHeight() {
        return getHeight(0);
    }
}
