package net.typho.big_shot_lib.mixin.impl.gl;

import com.mojang.blaze3d.platform.GlStateManager;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendEquation;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendingFactor;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager;
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction;
import net.typho.big_shot_lib.api.math.rect.NeoRect2i;
import net.typho.big_shot_lib.impl.client.rendering.opengl.state.NeoGlStateManagerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;

@Mixin(GlStateManager.class)
public abstract class GlStateManagerMixin {
    @Shadow
    public static void _glBindBuffer(int i, int j) {
    }

    @Inject(
            method = "_glBindBuffer",
            at = @At("TAIL")
    )
    private static void glBindBuffer(int type, int id, CallbackInfo ci) {
        NeoGlStateManagerImpl.boundBuffers.set(GlNamed.getEnum(GlBufferTarget.class, type), id);
    }

    @Redirect(
            method = "_glDeleteBuffers",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL32C;glBindBuffer(II)V"
            )
    )
    private static void glDeleteBuffers(int type, int id) {
        _glBindBuffer(type, id);
    }

    @Inject(
            method = "_glUseProgram",
            at = @At("TAIL")
    )
    private static void glUseProgram(int id, CallbackInfo ci) {
        NeoGlStateManagerImpl.boundProgram = id;
    }

    @Inject(
            method = "_glBindVertexArray",
            at = @At("TAIL")
    )
    private static void glBindVertexArray(int id, CallbackInfo ci) {
        NeoGlStateManagerImpl.boundVertexArray = id;
    }

    @Inject(
            method = "_glBindRenderbuffer",
            at = @At("TAIL")
    )
    private static void glBindRenderbuffer(int type, int id, CallbackInfo ci) {
        NeoGlStateManagerImpl.boundRenderbuffer = id;
    }

    @Inject(
            method = "_glBindFramebuffer",
            at = @At("TAIL")
    )
    private static void glBindFramebuffer(int type, int id, CallbackInfo ci) {
        if (type == GL_READ_FRAMEBUFFER) {
            NeoGlStateManagerImpl.boundReadFramebuffer = id;
        }
    }

    @Inject(
            method = "_blendEquation",
            at = @At("TAIL")
    )
    private static void blendEquation(int id, CallbackInfo ci) {
        NeoGlStateManagerImpl.currentBlendEquation = GlNamed.getEnum(GlBlendEquation.class, id);
    }

    @Inject(
            method = "_blendFunc",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glBlendFunc(II)V"
            )
    )
    private static void blendFunc(int src, int dest, CallbackInfo ci) {
        NeoGlStateManagerImpl.currentBlendFunction = new BlendFunction.Basic(
                GlNamed.getEnum(GlBlendingFactor.class, src),
                GlNamed.getEnum(GlBlendingFactor.class, dest)
        );
    }

    @Inject(
            method = "_blendFuncSeparate",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;glBlendFuncSeparate(IIII)V"
            )
    )
    private static void blendFuncSeparate(int src, int dest, int srcA, int destA, CallbackInfo ci) {
        NeoGlStateManagerImpl.currentBlendFunction = new BlendFunction.Separate(
                GlNamed.getEnum(GlBlendingFactor.class, src),
                GlNamed.getEnum(GlBlendingFactor.class, dest),
                GlNamed.getEnum(GlBlendingFactor.class, srcA),
                GlNamed.getEnum(GlBlendingFactor.class, destA)
        );
    }

    @Inject(
            method = "_scissorBox",
            at = @At("TAIL")
    )
    private static void scissorBox(int x, int y, int width, int height, CallbackInfo ci) {
        NeoGlStateManagerImpl.currentScissor = new NeoRect2i(x, y, x + width, y + height);
    }
}
