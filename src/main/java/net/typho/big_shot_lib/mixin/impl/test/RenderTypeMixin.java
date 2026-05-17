package net.typho.big_shot_lib.mixin.impl.test;

import kotlin.Pair;
import kotlin.Unit;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlCullFace;
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram;
import net.typho.big_shot_lib.api.client.rendering.opengl.state.*;
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType;
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat;
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier;
import net.typho.big_shot_lib.impl.IdentifierUtilKt;
import net.typho.big_shot_lib.impl.util.ImmutableExtensionKt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.BiFunction;

@Mixin(RenderType.class)
public abstract class RenderTypeMixin {
    @Shadow
    @Final
    @Mutable
    public static BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_TRANSLUCENT;

    static {
        ENTITY_TRANSLUCENT = Util.memoize((texture, affectsOutline) -> ImmutableExtensionKt.getExtensionValue(
                NeoRenderType.create(
                        new NeoIdentifier("minecraft", "entity_translucent"),
                        NeoVertexFormat.getNEW_ENTITY(),
                        new GlDrawState.Builder(
                                new GlShaderShard.FromInstance(
                                        () -> Objects.requireNonNull(GlProgram.getBUILTINS().getEntityTranslucent()),
                                        program -> Unit.INSTANCE,
                                        new Pair<>("Sampler0", new GlTextureBinding.FromLocation(IdentifierUtilKt.getNeo(texture)))
                                )
                        )
                                .cull(new GlCullShard.Enabled(GlCullFace.FRONT))
                                .lightmap(new GlLightmapShard(true))
                                .overlay(new GlOverlayShard(true))
                                .build(),
                        1536
                ),
                RenderType.class
        ));
    }
}
