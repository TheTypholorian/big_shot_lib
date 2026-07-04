package net.typho.big_shot_lib.mixin.impl.client.rendering.common;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureImpl;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResource;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResourceType;
import net.typho.big_shot_lib.api.client.rendering.common.GpuTexture;
import net.typho.big_shot_lib.api.client.rendering.common.Recyclable;
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureFormat;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;

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

    @Override
    public void clear(@NotNull Vector4fc color) {
        RenderSystem.getDevice().createCommandEncoder().clearColorTexture((GpuTextureImpl) (Object) this, color);
    }

    @Override
    public void upload(@NotNull NativeImage data) {
        RenderSystem.getDevice().createCommandEncoder().writeToTexture((GpuTextureImpl) (Object) this, data);
    }

    @Override
    public void upload(@NotNull NativeImage data, int mipLevel, int depthOrLayer, int destX, int destY) {
        RenderSystem.getDevice().createCommandEncoder().writeToTexture((GpuTextureImpl) (Object) this, data, mipLevel, depthOrLayer, destX, destY);
    }

    @Override
    public void upload(@NotNull ByteBuffer data, int mipLevel, int depthOrLayer, int destX, int destY, int width, int height) {
        RenderSystem.getDevice().createCommandEncoder().writeToTexture((GpuTextureImpl) (Object) this, data, mipLevel, depthOrLayer, destX, destY, width, height);
    }
}
