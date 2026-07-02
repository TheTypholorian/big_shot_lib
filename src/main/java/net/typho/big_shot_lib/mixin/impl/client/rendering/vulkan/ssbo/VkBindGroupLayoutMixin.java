package net.typho.big_shot_lib.mixin.impl.client.rendering.vulkan.ssbo;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalByteRef;
import com.mojang.blaze3d.vulkan.VkBindGroupLayout;
import net.typho.big_shot_lib.impl.client.rendering.vulkan.VkStorageBufferBindGroupEntryType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

import static org.lwjgl.vulkan.VK10.VK_DESCRIPTOR_TYPE_STORAGE_BUFFER;

@Mixin(VkBindGroupLayout.class)
public class VkBindGroupLayoutMixin {
    @Definition(id = "MatchException", type = MatchException.class)
    @Expression("throw new MatchException(null, null)")
    @WrapOperation(
            method = "create",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static void create(
            Operation<Void> original,
            @Local(argsOnly = true) List<VkBindGroupLayout.Entry> entries,
            @Local int i,
            @Local LocalByteRef descriptorType
    ) {
        if (entries.get(i).type() == VkStorageBufferBindGroupEntryType.INSTANCE) {
            descriptorType.set((byte) VK_DESCRIPTOR_TYPE_STORAGE_BUFFER);
        } else {
            original.call();
        }
    }
}
