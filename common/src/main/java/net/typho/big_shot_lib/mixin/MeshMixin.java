package net.typho.big_shot_lib.mixin;

import net.typho.big_shot_lib.api.client.opengl.buffers.Mesh;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;
import static org.lwjgl.opengl.GL33.glBindSampler;

@Mixin(Mesh.class)
public class MeshMixin {
    @Inject(
            method = "draw",
            at = @At("HEAD")
    )
    private void draw(CallbackInfo ci) {
        for (int i = 0; i < glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS); i++) {
            glBindSampler(i, 0);
        }
    }
}
