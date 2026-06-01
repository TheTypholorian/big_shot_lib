package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram;
import net.typho.big_shot_lib.api.client.rendering.util.NeoGuiGraphics;
import net.typho.big_shot_lib.api.util.ImmutableExtensionKt;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin implements NeoGuiGraphics {
    @Shadow
    @Final
    private PoseStack pose;

    @Override
    public void blitWithShader(@NotNull GlProgram shader, @NotNull Identifier location, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        this.blitWithShader(shader, location, x, y, 0, uOffset, vOffset, uWidth, vHeight, 256, 256);
    }

    @Override
    public void blitWithShader(@NotNull GlProgram shader, @NotNull Identifier location, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        this.blitWithShader(shader, location, x, x + uWidth, y, y + vHeight, blitOffset, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    @Override
    public void blitWithShader(@NotNull GlProgram shader, @NotNull Identifier location, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        this.blitWithShader(shader, location, x, x + width, y, y + height, 0, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    @Override
    public void blitWithShader(@NotNull GlProgram shader, @NotNull Identifier location, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight) {
        this.blitWithShader(shader, location, x, y, width, height, uOffset, vOffset, width, height, textureWidth, textureHeight);
    }

    @Unique
    private void blitWithShader(@NotNull GlProgram shader, @NotNull Identifier location, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
        innerBlitWithShader(shader, location, x1, x2, y1, y2, blitOffset, uOffset / textureWidth, (uOffset + uWidth) / textureWidth, vOffset / textureHeight, (vOffset + vHeight) / textureHeight);
    }

    @Unique
    private void innerBlitWithShader(@NotNull GlProgram shader, @NotNull Identifier location, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        RenderSystem.setShaderTexture(0, location);
        RenderSystem.setShader(() -> ImmutableExtensionKt.getExtensionValue(shader, ShaderInstance.class)); // TODO
        RenderSystem.enableBlend();

        Matrix4f mat = pose.last().pose();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        builder.vertex(mat, x1, y1, blitOffset).textureUV(minU, minV).end();
        builder.vertex(mat, x1, y2, blitOffset).textureUV(minU, maxV).end();
        builder.vertex(mat, x2, y2, blitOffset).textureUV(maxU, maxV).end();
        builder.vertex(mat, x2, y1, blitOffset).textureUV(maxU, minV).end();

        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.disableBlend();
    }

    @Unique
    private void innerBlitWithShader(@NotNull GlProgram shader, @NotNull Identifier location, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV, float red, float green, float blue, float alpha) {
        RenderSystem.setShaderTexture(0, location);
        RenderSystem.setShader(() -> ImmutableExtensionKt.getExtensionValue(shader, ShaderInstance.class));
        RenderSystem.enableBlend();

        Matrix4f mat = pose.last().pose();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        builder.vertex(mat, x1, y1, blitOffset).textureUV(minU, minV).color(red, green, blue, alpha).end();
        builder.vertex(mat, x1, y2, blitOffset).textureUV(minU, maxV).color(red, green, blue, alpha).end();
        builder.vertex(mat, x2, y2, blitOffset).textureUV(maxU, maxV).color(red, green, blue, alpha).end();
        builder.vertex(mat, x2, y1, blitOffset).textureUV(maxU, minV).color(red, green, blue, alpha).end();

        BufferUploader.drawWithShader(builder.buildOrThrow());

        RenderSystem.disableBlend();
    }
}
