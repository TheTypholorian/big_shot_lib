package net.typho.big_shot_lib.mixin.impl.client.rendering.opengl;

import com.mojang.blaze3d.opengl.GlShaderModule;
import com.mojang.blaze3d.shaders.ShaderType;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResource;
import net.typho.big_shot_lib.api.client.rendering.common.GpuResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed;
import net.typho.big_shot_lib.api.client.rendering.opengl.GlResource;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GlShaderModule.class)
public abstract class GlShaderModuleMixin implements GlResource, GpuResource, GlNamed {
    @Shadow
    @Final
    private ShaderType type;

    @Shadow
    public abstract int getShaderId();

    @Override
    public @NotNull GpuResourceType getType() {
        return switch (type) {
            case VERTEX -> GpuResourceType.VERTEX_SHADER;
            case FRAGMENT -> GpuResourceType.FRAGMENT_SHADER;
        };
    }

    @Override
    public int getGlId() {
        return getShaderId();
    }
}
