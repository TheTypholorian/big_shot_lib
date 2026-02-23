package net.typho.big_shot_lib.mixin.event;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.typho.big_shot_lib.BigShotClientEventStorage;
import net.typho.big_shot_lib.api.client.util.events.RenderEventData;
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
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/Camera;)V"
            )
    )
    private void renderLevel(
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
        Matrix4f frustum = frustumMatrix.translate(camera.getPosition().toVector3f().mul(-1), new Matrix4f());
        Matrix4f iProj = projectionMatrix.invertPerspective(new Matrix4f());
        RenderEventData data = new RenderEventData(
                renderBuffers.bufferSource(),
                new net.typho.big_shot_lib.api.client.opengl.state.Camera(
                        camera.getPosition().toVector3f(),
                        camera.getXRot(),
                        camera.getYRot(),
                        camera.rotation()
                ),
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

        BigShotClientEventStorage.onLevelRenderEnd.forEach(event -> event.invoke(data));
    }
}
