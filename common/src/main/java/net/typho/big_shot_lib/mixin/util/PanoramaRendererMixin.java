package net.typho.big_shot_lib.mixin.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.typho.big_shot_lib.BigShotClientEventStorage;
import net.typho.big_shot_lib.api.client.util.panoramas.PanoramaSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PanoramaRenderer.class)
public class PanoramaRendererMixin {
    @Unique
    private final long big_shot_lib$startTime = System.currentTimeMillis();

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/CubeMap;render(Lnet/minecraft/client/Minecraft;FF)V"
            )
    )
    private void render(CubeMap instance, Minecraft minecraft, float xRot, float yRot, Operation<Void> original) {
        PanoramaSet panorama = BigShotClientEventStorage.panorama;

        if (panorama == null) {
            original.call(instance, minecraft, xRot, yRot);
        } else {
            if (panorama.interval == null) {
                original.call(
                        BigShotClientEventStorage.panoramaCubeMaps.get(
                                panorama.textures.get((int) (big_shot_lib$startTime % panorama.textures.size()))
                        ),
                        minecraft,
                        xRot,
                        yRot
                );
            } else {
                long time = System.currentTimeMillis();
                long fade = panorama.fade == null ? Math.min(1000, panorama.interval) : panorama.fade;
                int index = (int) ((time / panorama.interval) % panorama.textures.size());

                original.call(
                        BigShotClientEventStorage.panoramaCubeMaps.get(panorama.textures.get(index)),
                        minecraft,
                        xRot,
                        yRot
                );

                // TODO
                /*
                long localTime = time % panorama.interval;

                if (localTime - panorama.interval >= fade) {
                    original.call(
                            BigShotClientEventStorage.panoramaCubeMaps.get(
                                    panorama.textures.get(index == panorama.textures.size() - 1 ? 0 : (index + 1))
                            ),
                            minecraft,
                            xRot,
                            yRot
                            //alpha * (localTime - panorama.interval) / fade
                    );
                }
                 */
            }
        }
    }
}
