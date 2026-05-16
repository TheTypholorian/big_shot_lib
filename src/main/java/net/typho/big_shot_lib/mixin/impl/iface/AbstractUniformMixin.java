package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.shaders.AbstractUniform;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractUniform.class)
public abstract class AbstractUniformMixin implements ImmutableExtension<GlUniform> {
    @Shadow
    public abstract void set(int i);

    @Shadow
    public abstract void set(float f);

    @Shadow
    public abstract void set(float[] fs);

    @Shadow
    public abstract void set(int i, int j);

    @Shadow
    public abstract void set(float f, float g);

    @Shadow
    public abstract void set(Matrix3f matrix3f);

    @Shadow
    public abstract void set(Matrix4f matrix4f);

    @Shadow
    public abstract void set(int i, int j, int k);

    @Shadow
    public abstract void set(float f, float g, float h);

    @Shadow
    public abstract void set(int i, int j, int k, int l);

    @Shadow
    public abstract void set(float f, float g, float h, float i);

    @Unique
    private final GlUniform big_shot_lib$extension_value = new GlUniform() {
        @Override
        public void set(float f1) {
            AbstractUniformMixin.this.set(f1);
        }

        @Override
        public void set(float f1, float f2) {
            AbstractUniformMixin.this.set(f1, f2);
        }

        @Override
        public void set(float f1, float f2, float f3) {
            AbstractUniformMixin.this.set(f1, f2, f3);
        }

        @Override
        public void set(float f1, float f2, float f3, float f4) {
            AbstractUniformMixin.this.set(f1, f2, f3, f4);
        }

        @Override
        public void set(@NotNull float[] array) {
            AbstractUniformMixin.this.set(array);
        }

        @Override
        public void set(int i1) {
            AbstractUniformMixin.this.set(i1);
        }

        @Override
        public void set(int i1, int i2) {
            AbstractUniformMixin.this.set(i1, i2);
        }

        @Override
        public void set(int i1, int i2, int i3) {
            AbstractUniformMixin.this.set(i1, i2, i3);
        }

        @Override
        public void set(int i1, int i2, int i3, int i4) {
            AbstractUniformMixin.this.set(i1, i2, i3, i4);
        }

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
    };

    @Override
    public GlUniform getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
