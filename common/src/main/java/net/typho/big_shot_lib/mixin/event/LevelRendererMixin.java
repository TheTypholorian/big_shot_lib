package net.typho.big_shot_lib.mixin.event;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.typho.big_shot_lib.api.client.rendering.event.PostProcessEvent;
import net.typho.big_shot_lib.api.client.rendering.event.RenderData;
import net.typho.big_shot_lib.api.client.rendering.state.GlStateStack;
import org.jetbrains.annotations.Nullable;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Shadow
    @Nullable
    private ClientLevel level;

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;addLateDebugPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/FogParameters;)V"
            )
    )
    private void renderLevel(
            GraphicsResourceAllocator graphicsResourceAllocator,
            DeltaTracker deltaTracker,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f frustumMatrix,
            Matrix4f projectionMatrix,
            CallbackInfo ci
    ) {
        assert level != null;
        PostProcessEvent.Companion.invoke(new RenderData(
                renderBuffers.bufferSource(),
                camera,
                level,
                projectionMatrix,
                frustumMatrix,
                new FrustumIntersection(projectionMatrix.mul(frustumMatrix.translate(camera.getPosition().toVector3f().mul(-1), new Matrix4f()), new Matrix4f())),
                Minecraft.getInstance().getMainRenderTarget().width,
                Minecraft.getInstance().getMainRenderTarget().height
        ));
        GlStateStack.ensureAllEmpty();
    }
}
