package net.typho.big_shot_lib.impl.mixin;

import net.minecraft.client.renderer.culling.Frustum;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoCamera;
import net.typho.big_shot_lib.api.math.vec.NeoVec2f;
import net.typho.big_shot_lib.api.math.vec.NeoVec3f;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.LevelRenderer;

//? if >=1.21.10 {
/*import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.RenderBuffers;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer;
import net.typho.big_shot_lib.api.client.util.event.RenderEventData;
import net.typho.big_shot_lib.impl.client.util.BigShotClientEvents;
import org.jetbrains.annotations.Nullable;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///? }

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    //? if >=1.21.10 {
    /*@Shadow
    @Nullable
    private ClientLevel level;

    @Shadow
    @Final
    private LevelTargetBundle targets;

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;addLateDebugPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/world/phys/Vec3;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lnet/minecraft/client/renderer/culling/Frustum;)V"
            )
    )
    private void renderLevel(
            GraphicsResourceAllocator graphicsResourceAllocator,
            DeltaTracker deltaTracker,
            boolean renderBlockOutline,
            Camera camera,
            Matrix4f frustumMatrix,
            Matrix4f projectionMatrix,
            Matrix4f cullingProjectionMatrix,
            GpuBufferSlice shaderFog,
            Vector4f fogColor,
            boolean renderSky,
            CallbackInfo ci,
            @Local FrameGraphBuilder graph,
            @Local Frustum frustum
    ) {
        if (!BigShotClientEvents.INSTANCE.getLevelRenderEnd().isEmpty()) {
            FramePass pass = graph.addPass(BigShotApi.id("post").toString());

            targets.main = pass.readsAndWrites(targets.main);

            pass.executes(() -> {
                assert level != null;

                //? if <=1.21.10 {
                RenderEventData data = new RenderEventData(
                        new NeoCamera(
                                new NeoVec3f(camera.getPosition()),
                                new NeoVec2f(camera.getXRot(), camera.getYRot()),
                                camera.rotation()
                        ),
                        level,
                        projectionMatrix,
                        frustumMatrix,
                        ((FrustumAccessor) frustum).big_shot_lib$getFrustmIntersection(),
                        GlFramebuffer.MAIN
                );
                //? } else {
                /^RenderEventData data = new RenderEventData(
                        new NeoCamera(
                                new NeoVec3f(camera.position()),
                                new NeoVec2f(camera.xRot(), camera.yRot()),
                                camera.rotation()
                        ),
                        level,
                        projectionMatrix,
                        frustumMatrix,
                        ((FrustumAccessor) frustum).big_shot_lib$getFrustmIntersection(),
                        GlFramebuffer.MAIN
                );
                ^///? }

                BigShotClientEvents.INSTANCE.getLevelRenderEnd().forEach(event -> event.invoke(data));
            });
        }
    }
    *///? }
}
