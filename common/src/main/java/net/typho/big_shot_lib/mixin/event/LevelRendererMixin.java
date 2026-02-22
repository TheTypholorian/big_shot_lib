package net.typho.big_shot_lib.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.RenderBuffers;
import net.typho.big_shot_lib.BigShotClientEventStorage;
import net.typho.big_shot_lib.api.client.registration.events.RenderEventData;
import net.typho.big_shot_lib.api.client.rendering.state.GlFlag;
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

    @Shadow
    @Final
    private LevelTargetBundle targets;

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
            Matrix4f frustumMatrix,
            Matrix4f projectionMatrix,
            CallbackInfo ci,
            @Local FrameGraphBuilder graph
    ) {
        FramePass pass = graph.addPass("big_shot_lib:post");

        targets.main = pass.readsAndWrites(targets.main);

        pass.executes(() -> {
            assert level != null;
            Matrix4f frustum = frustumMatrix.translate(camera.getPosition().toVector3f().mul(-1), new Matrix4f());
            Matrix4f iProj = projectionMatrix.invertPerspective(new Matrix4f());
            RenderEventData data = new RenderEventData(
                    renderBuffers.bufferSource(),
                    camera,
                    level,
                    projectionMatrix,
                    iProj,
                    frustum,
                    new Matrix4f(frustumMatrix)
                            .mulLocal(iProj.mul(projectionMatrix, new Matrix4f()))
                            .invert(),
                    new FrustumIntersection(projectionMatrix.mul(frustum, new Matrix4f())),
                    Minecraft.getInstance().getMainRenderTarget().width,
                    Minecraft.getInstance().getMainRenderTarget().height
            );
            GlFlag.BLEND.disable();

            BigShotClientEventStorage.onLevelRenderEnd.forEach(event -> event.invoke(data));
        });
    }
}
