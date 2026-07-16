package net.typho.big_shot_lib.mixin.client.rendering.common;

import com.mojang.blaze3d.systems.RenderPassBackend;
import net.typho.big_shot_lib.client.api.ext.RenderPassExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderPassBackend.class)
public interface RenderPassBackendMixin extends RenderPassExtension {
}
