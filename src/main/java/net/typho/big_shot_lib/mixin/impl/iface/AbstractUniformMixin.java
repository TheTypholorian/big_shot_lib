package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.shaders.AbstractUniform;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlUniform;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractUniform.class)
public abstract class AbstractUniformMixin implements GlUniform {
    @Shadow
    public abstract void set(Matrix3f matrix);

    @Shadow
    public abstract void set(Matrix4f matrix);

    @Override
    public void set(@NotNull int[] array) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(int[])");
    }

    @Override
    public void set(double d1) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(double)");
    }

    @Override
    public void set(double d1, double d2) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(double, double)");
    }

    @Override
    public void set(double d1, double d2, double d3) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(double, double, double)");
    }

    @Override
    public void set(double d1, double d2, double d3, double d4) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(double, double, double, double)");
    }

    @Override
    public void set(@NotNull double[] array) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(double[])");
    }

    @Override
    public void set(@NotNull Matrix2f mat, boolean transpose) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(Matrix2f)");
    }

    @Override
    public void set(@NotNull Matrix3f mat, boolean transpose) {
        if (transpose) {
            mat = mat.transpose(new Matrix3f());
        }

        AbstractUniformMixin.this.set(mat);
    }

    @Override
    public void set(@NotNull Matrix4f mat, boolean transpose) {
        if (transpose) {
            mat = mat.transpose(new Matrix4f());
        }

        AbstractUniformMixin.this.set(mat);
    }

    @Override
    public void set(@NotNull Matrix3x2f mat, boolean transpose) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(Matrix3x2f)");
    }

    @Override
    public void set(@NotNull Matrix4x3f mat, boolean transpose) {
        throw new UnsupportedOperationException("Minecraft AbstractUniform.set(Matrix4x3f)");
    }
}
