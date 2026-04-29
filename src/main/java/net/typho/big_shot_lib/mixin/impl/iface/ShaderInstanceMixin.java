package net.typho.big_shot_lib.mixin.impl.iface;

import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.ShaderInstance;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram;
import net.typho.big_shot_lib.impl.client.rendering.util.NeoVertexFormatImpl;
import net.typho.big_shot_lib.impl.util.MutableExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >1.21.1 {
/*import dev.kikugie.fletching_table.annotation.MixinIgnore;

@MixinIgnore
*///? }
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin implements MutableExtension<GlProgram> {
    @Unique
    private GlProgram big_shot_lib$program;

    @Override
    public GlProgram getBig_shot_lib$extension_value() {
        return big_shot_lib$program;
    }

    @Override
    public void setBig_shot_lib$extension_value(GlProgram glProgram) {
        big_shot_lib$program = glProgram;
    }

    @Inject(
            method = {
                    "close",
                    "clear",
                    "apply",
                    "markDirty",
                    "updateLocations",
                    "parseSamplerNode",
                    "setSampler",
                    "parseUniformNode",
                    "attachToProgram",
                    "setDefaultUniforms"
            },
            at = @At("HEAD"),
            cancellable = true
    )
    private void redirect(CallbackInfo ci) {
        if (big_shot_lib$program != null) {
            ci.cancel();
        }
    }

    @Inject(
            method = "getVertexProgram",
            at = @At("HEAD")
    )
    private void getVertexProgram(CallbackInfoReturnable<Program> cir) {
        if (big_shot_lib$program != null) {
            throw new UnsupportedOperationException("Cannot get the vertex program of a big shot lib shader through Minecraft ShaderInstance");
        }
    }

    @Inject(
            method = "getFragmentProgram",
            at = @At("HEAD")
    )
    private void getFragmentProgram(CallbackInfoReturnable<Program> cir) {
        if (big_shot_lib$program != null) {
            throw new UnsupportedOperationException("Cannot get the fragment program of a big shot lib shader through Minecraft ShaderInstance");
        }
    }

    @Inject(
            method = "getVertexFormat",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getVertexFormat(CallbackInfoReturnable<VertexFormat> cir) {
        if (big_shot_lib$program != null) {
            cir.setReturnValue(((NeoVertexFormatImpl) big_shot_lib$program.getFormat()).inner);
        }
    }

    @Inject(
            method = "getName",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getName(CallbackInfoReturnable<String> cir) {
        if (big_shot_lib$program != null) {
            cir.setReturnValue(big_shot_lib$program.getLocation().toString());
        }
    }

    @Inject(
            method = "getId",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getId(CallbackInfoReturnable<Integer> cir) {
        if (big_shot_lib$program != null) {
            cir.setReturnValue(big_shot_lib$program.getGlId());
        }
    }

    @Inject(
            method = "getUniform",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getUniform(CallbackInfoReturnable<Integer> cir) {
        if (big_shot_lib$program != null) {
            cir.setReturnValue(null);
        }
    }
}
