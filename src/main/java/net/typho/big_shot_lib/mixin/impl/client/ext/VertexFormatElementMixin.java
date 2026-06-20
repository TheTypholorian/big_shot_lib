package net.typho.big_shot_lib.mixin.impl.client.ext;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.typho.big_shot_lib.api.client.ext.VertexFormatElementExtension;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlVertexElementReadType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VertexFormatElement.class)
public class VertexFormatElementMixin implements VertexFormatElementExtension {
    @Shadow
    @Final
    private int count;
    @Shadow
    @Final
    private VertexFormatElement.Type type;
    @Unique
    private GlVertexElementReadType big_shot_lib$outType;

    @Override
    public @Nullable GlVertexElementReadType getBig_shot_lib$outType() {
        return big_shot_lib$outType;
    }

    @Override
    public void setBig_shot_lib$outType(@Nullable GlVertexElementReadType glVertexElementReadType) {
        big_shot_lib$outType = glVertexElementReadType;
    }

    @Inject(
            method = "setupBufferState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void setupBufferState(int stateIndex, long offset, int stride, CallbackInfo ci) {
        if (big_shot_lib$outType != null) {
            big_shot_lib$outType.setupBufferState(count, type.glType(), stride, offset, stateIndex);
            ci.cancel();
        }
    }
}
