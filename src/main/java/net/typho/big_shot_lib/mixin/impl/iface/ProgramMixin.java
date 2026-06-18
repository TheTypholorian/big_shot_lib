package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.shaders.Program;
import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShader;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShaderType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static org.lwjgl.opengl.GL20.*;

@Mixin(Program.class)
public abstract class ProgramMixin implements GlShader {
    @Shadow
    @Final
    private Program.Type type;
    @Shadow
    @Final
    private String name;
    @Shadow
    private int id;

    @Shadow
    public abstract void close();

    @Override
    @NotNull
    public GlShaderType getShaderType() {
        return switch (type) {
            case VERTEX -> GlShaderType.VERTEX;
            case FRAGMENT -> GlShaderType.FRAGMENT;
        };
    }

    @Override
    @NotNull
    public String getSource() {
        return glGetShaderSource(getGlId());
    }

    @Override
    public void setSource(@NotNull String s) {
        glShaderSource(getGlId(), s);
    }

    @Override
    @NotNull
    public String getInfoLog() {
        return glGetShaderInfoLog(getGlId(), 4096).trim();
    }

    @Override
    public boolean compile() {
        glCompileShader(getGlId());

        return glGetShaderi(getGlId(), GL_COMPILE_STATUS) == GL_TRUE;
    }

    @Override
    @NotNull
    public GlResourceType getType() {
        return getShaderType().resourceType;
    }

    @Override
    public boolean getFreed() {
        return id == -1;
    }

    @Override
    public int getGlId() {
        return id;
    }

    @Override
    @NotNull
    public Identifier getLocation() {
        return Identifier.parse(name);
    }
}
