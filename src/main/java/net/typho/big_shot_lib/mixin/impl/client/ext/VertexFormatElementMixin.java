package net.typho.big_shot_lib.mixin.impl.client.ext;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/VertexFormatElement;supportsUsage(ILcom/mojang/blaze3d/vertex/VertexFormatElement$Usage;)Z"
            )
    )
    private boolean init(VertexFormatElement instance, int index, VertexFormatElement.Usage usage, Operation<Boolean> original) {
        if (usage == null) {
            return true;
        }

        return original.call(instance, index, usage);
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
