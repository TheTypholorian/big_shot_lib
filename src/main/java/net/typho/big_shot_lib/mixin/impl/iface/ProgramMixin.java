package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.shaders.Program;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType;
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier;
import net.typho.big_shot_lib.impl.util.ImmutableExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

@Mixin(Program.class)
public abstract class ProgramMixin implements ImmutableExtension<GlShaderExtensionValue> {
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

    @Unique
    private final GlShaderExtensionValue big_shot_lib$extension_value = new GlShaderExtensionValue() {
        @Override
        @NotNull
        public Program getBig_shot_lib$extension_value() {
            return (Program) (Object) ProgramMixin.this;
        }

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
        public NeoIdentifier getLocation() {
            return new NeoIdentifier(name);
        }

        @Override
        public void free() {
            ProgramMixin.this.close();
        }
    };

    @Override
    public GlShaderExtensionValue getBig_shot_lib$extension_value() {
        return big_shot_lib$extension_value;
    }
}
