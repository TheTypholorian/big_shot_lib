package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan.ssbo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vulkan.glsl.IntermediaryShaderModule;
import com.mojang.blaze3d.vulkan.glsl.ShaderCompileException;
import net.typho.big_shot_lib.impl.client.rendering.vulkan.ssbo.IntermediaryShaderModuleExtension;
import net.typho.big_shot_lib.impl.client.rendering.vulkan.ssbo.SpvStorageBuffer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.spvc.Spvc;
import org.lwjgl.util.spvc.SpvcReflectedResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.util.spvc.Spv.SpvDecorationBinding;

@Mixin(IntermediaryShaderModule.class)
public class IntermediaryShaderModuleMixin implements IntermediaryShaderModuleExtension {
    @Shadow
    private static void throwIfError(int result, String message) throws ShaderCompileException {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static int getDecorationOffset(long compiler, SpvcReflectedResource resource, int decoration, IntBuffer returnBuffer) throws ShaderCompileException {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Unique
    private List<@NotNull SpvStorageBuffer> big_shot_lib$storageBuffers = new ArrayList<>();

    @Override
    public @NotNull List<@NotNull SpvStorageBuffer> getBig_shot_lib$storageBuffers() {
        return big_shot_lib$storageBuffers;
    }

    @Override
    public void setBig_shot_lib$storageBuffers(@NotNull List<@NotNull SpvStorageBuffer> storageBuffers) {
        big_shot_lib$storageBuffers = storageBuffers;
    }

    @Inject(
            method = "createFromSpirv",
            at = @At("HEAD")
    )
    private static void createFromSpirv1(
            String filename,
            ByteBuffer spirv,
            CallbackInfoReturnable<IntermediaryShaderModule> cir,
            @Share("storageBuffers") LocalRef<List<SpvStorageBuffer>> storageBuffers
    ) {
        storageBuffers.set(new ArrayList<>());
    }

    @Inject(
            method = "createFromSpirv",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/util/spvc/Spvc;spvc_resources_get_resource_list_for_type(JILorg/lwjgl/PointerBuffer;Lorg/lwjgl/PointerBuffer;)I",
                    ordinal = 0
            )
    )
    private static void createFromSpirv2(
            String filename,
            ByteBuffer spirv,
            CallbackInfoReturnable<IntermediaryShaderModule> cir,
            @Local(ordinal = 3) long spvcResources,
            @Local(ordinal = 0) PointerBuffer pointer,
            @Local(ordinal = 1) PointerBuffer countPointer,
            @Local(ordinal = 2) long compiler,
            @Local IntBuffer intReturnBuffer,
            @Share("storageBuffers") LocalRef<List<SpvStorageBuffer>> storageBuffers
    ) throws ShaderCompileException {
        throwIfError(Spvc.spvc_resources_get_resource_list_for_type(spvcResources, Spvc.SPVC_RESOURCE_TYPE_STORAGE_BUFFER, pointer, countPointer), "Couldn't list shader storage buffers");
        long spvcList = pointer.get(0);
        long spvcCount = countPointer.get(0);
        SpvcReflectedResource.Buffer resources = SpvcReflectedResource.create(spvcList, (int)spvcCount);

        for (int i = 0; i < spvcCount; ++i) {
            SpvcReflectedResource resource = resources.get(i);
            String name = resource.nameString();
            int bindingOffset = getDecorationOffset(compiler, resource, SpvDecorationBinding, intReturnBuffer);
            storageBuffers.get().add(new SpvStorageBuffer(name, bindingOffset));
        }
    }

    @ModifyReturnValue(
            method = "createFromSpirv",
            at = @At("RETURN")
    )
    private static IntermediaryShaderModule createFromSpirv3(
            IntermediaryShaderModule original,
            @Share("storageBuffers") LocalRef<List<SpvStorageBuffer>> storageBuffers
    ) {
        ((IntermediaryShaderModuleExtension) (Object) original).setBig_shot_lib$storageBuffers(storageBuffers.get());
        return original;
    }
}
