package net.typho.big_shot_lib.mixin.client.rendering.opengl.ssbo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.opengl.GlProgram;
import com.mojang.blaze3d.opengl.Uniform;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import net.typho.big_shot_lib.client.impl.rendering.common.StorageBufferUniformType;
import net.typho.big_shot_lib.client.impl.rendering.opengl.ssbo.UboUniformExtension;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL43.*;

@Mixin(GlProgram.class)
public class GlProgramMixin {
    @Shadow
    @Final
    private Map<String, Uniform> uniformsByName;

    @Shadow
    @Final
    private int programId;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private String debugLabel;

    @WrapOperation(
            method = "setupBindGroupLayouts",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
                    ordinal = 0
            )
    )
    private <E extends BindGroupLayout.UniformDescription> Iterator<E> setupBindGroupLayouts(List<E> instance, Operation<Iterator<E>> original) {
        List<E> ret = new ArrayList<>();
        int nextSSBOBinding = 0;

        for (E uniform : instance) {
            if (uniform.type() == StorageBufferUniformType.INSTANCE) {
                int index = glGetProgramResourceIndex(programId, GL_SHADER_STORAGE_BLOCK, uniform.name());

                if (index == -1) {
                    LOGGER.warn("{} shader program does not use ssbo {} defined in the pipeline. This might be a bug.", debugLabel, uniform.name());
                } else {
                    int ssboBinding = nextSSBOBinding++;
                    glShaderStorageBlockBinding(programId, index, ssboBinding);
                    Uniform.Ubo ssbo = new Uniform.Ubo(ssboBinding);
                    ((UboUniformExtension) (Object) ssbo).setBig_shot_lib$isSsbo(true);
                    uniformsByName.put(uniform.name(), ssbo);
                }
            } else {
                ret.add(uniform);
            }
        }

        return original.call(ret);
    }
}
