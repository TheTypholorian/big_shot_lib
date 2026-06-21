package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlResource;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;

@Mixin(RenderTarget.class)
public class RenderTargetMixin implements GlResource, GlNamed {
    @Shadow
    @Nullable
    protected GpuTexture colorTexture;
    @Shadow
    @Nullable
    protected GpuTexture depthTexture;

    @Override
    public int getGlId() {
        GlDevice device = ((GlDevice) RenderSystem.getDevice());
        return device.frameBufferCache().getFbo(device.directStateAccess(), Collections.singletonList(colorTexture), depthTexture);
    }
}
