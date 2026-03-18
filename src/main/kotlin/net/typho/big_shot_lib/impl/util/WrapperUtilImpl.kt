package net.typho.big_shot_lib.impl.util

//? if >=1.21 {
//import com.mojang.blaze3d.vertex.ByteBufferBuilder
//? }

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.serialization.DataResult
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.client.rendering.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.NeoVertexFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.NeoRegistryAccess
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoTagKey
import net.typho.big_shot_lib.impl.client.rendering.NeoVertexFormatImpl
import net.typho.big_shot_lib.impl.mojang
import net.typho.big_shot_lib.impl.neo
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

object WrapperUtilImpl : WrapperUtil {
    override fun wrap(manager: ResourceManager): NeoResourceManager {
        return object : NeoResourceManager {
            override fun getNamespaces(): Set<String> {
                return manager.namespaces
            }

            override fun getResourceStack(location: NeoIdentifier): List<Resource> {
                return manager.getResourceStack(location.mojang)
            }

            override fun listResources(
                folder: String,
                predicate: (id: NeoIdentifier) -> Boolean
            ): Map<NeoIdentifier, Resource> {
                return manager.listResources(folder) { predicate(it.neo) }.mapKeys { (key, value) -> key.neo }
            }

            override fun listResourceStacks(
                folder: String,
                predicate: (id: NeoIdentifier) -> Boolean
            ): Map<NeoIdentifier, List<Resource>> {
                return manager.listResourceStacks(folder) { predicate(it.neo) }.mapKeys { (key, value) -> key.neo }
            }

            override fun listPacks(): Stream<PackResources> {
                return manager.listPacks()
            }

            override fun getResource(location: NeoIdentifier): Optional<Resource> {
                return manager.getResource(location.mojang)
            }
        }
    }

    override fun wrap(target: RenderTarget): GlFramebuffer {
        return target.getExtensionValue()
    }

    override fun <T : Any> wrap(registry: Registry<T>): NeoRegistry<T> {
        return object : NeoRegistry<T> {
            override val key: NeoResourceKey<out Registry<T>> = registry.key().neo

            override fun get(value: NeoIdentifier): T? {
                return registry.get(value.mojang)
            }

            override fun getKey(value: T): NeoResourceKey<T> {
                return registry.getResourceKey(value).orElseThrow().neo
            }

            override fun contains(value: NeoIdentifier): Boolean {
                return registry.containsKey(value.mojang)
            }

            override fun keys(): Set<NeoResourceKey<T>> {
                return registry.keySet().map { NeoResourceKey<T>(key.location, it.neo) }.toSet()
            }

            override fun entries(): Set<Pair<NeoResourceKey<T>, T>> {
                return registry.entrySet().map { entry -> entry.key.neo to entry.value }.toSet()
            }

            override fun values(): Collection<T> {
                return registry.entrySet().map { it.value }
            }

            override fun getTag(key: NeoTagKey<T>): Set<T>? {
                return registry.getTag(key.mojang)
                    .map { set ->
                        set.stream()
                            .map { it.value() }
                            .collect(Collectors.toSet())
                    }
                    .getOrNull()
            }

            override fun tags(): Set<NeoTagKey<T>> {
                return registry.tags.map { it.first.neo }.collect(Collectors.toSet())
            }
        }
    }

    override fun wrap(access: RegistryAccess): NeoRegistryAccess {
        return object : NeoRegistryAccess {
            override fun <T : Any> registry(key: NeoResourceKey<Registry<T>>): NeoRegistry<T>? {
                return access.registry(key.mojang).map { wrap(it) }.getOrNull()
            }
        }
    }

    override fun wrap(
        quad: BakedQuad
    ): NeoBakedQuad {
        return quad.getExtensionValue()
    }

    override fun wrap(consumer: VertexConsumer): NeoVertexConsumer {
        return object : NeoVertexConsumer {
            //? if >=1.21 {
           // var started = false
            //? }

            override fun vertex(
                x: Float,
                y: Float,
                z: Float
            ): NeoVertexConsumer {
                //? if >=1.21 {
                /*if (started) {
                    throw IllegalStateException("Forgot to call endVertex")
                }

                started = true
                consumer.addVertex(x, y, z)
                *///? } else {
                consumer.vertex(x.toDouble(), y.toDouble(), z.toDouble())
                //? }
                return this
            }

            override fun color(
                r: Int,
                g: Int,
                b: Int,
                a: Int
            ): NeoVertexConsumer {
                //? if >=1.21 {
                //consumer.setColor(r, g, b, a)
                //? } else {
                consumer.color(r, g, b, a)
                //? }
                return this
            }

            override fun textureUV(
                u: Float,
                v: Float
            ): NeoVertexConsumer {
                //? if >=1.21 {
                //consumer.setUv(u, v)
                //? } else {
                consumer.uv(u, v)
                //? }
                return this
            }

            override fun overlayUV(
                u: Int,
                v: Int
            ): NeoVertexConsumer {
                //? if >=1.21 {
                //consumer.setUv1(u, v)
                //? } else {
                consumer.overlayCoords(u, v)
                //? }
                return this
            }

            override fun lightUV(
                u: Int,
                v: Int
            ): NeoVertexConsumer {
                //? if >=1.21 {
                //consumer.setUv2(u, v)
                //? } else {
                consumer.uv2(u, v)
                //? }
                return this
            }

            override fun normal(
                x: Float,
                y: Float,
                z: Float
            ): NeoVertexConsumer {
                //? if >=1.21 {
                //consumer.setNormal(x, y, z)
                //? } else {
                consumer.normal(x, y, z)
                //? }
                return this
            }

            override fun normal(
                pose: PoseStack.Pose,
                x: Float,
                y: Float,
                z: Float
            ): NeoVertexConsumer {
                //? if >=1.21 {
                //consumer.setNormal(pose, x, y, z)
                //? } else if >=1.20.5 {
                consumer.normal(pose, x, y, z)
                //? } else {
                /*consumer.normal(pose.normal(), x, y, z)
                *///? }
                return this
            }

            override fun endVertex() {
                //? if <1.21 {
                consumer.endVertex()
                //? } else {
                //started = false
                //? }
            }
        }
    }

    override fun <T : Any> wrap(key: ResourceKey<T>): NeoResourceKey<T> {
        return key.neo
    }

    override fun <T : Any> wrap(key: TagKey<T>): NeoTagKey<T> {
        return key.neo
    }

    override fun <T : Any> unwrap(key: NeoResourceKey<T>): ResourceKey<T> {
        return key.mojang
    }

    override fun <T : Any> unwrap(key: NeoTagKey<T>): TagKey<T> {
        return key.mojang
    }

    override fun createVertexFormatBuilder(): NeoVertexFormat.Builder {
        return NeoVertexFormatImpl.BuilderImpl()
    }

    override fun positionVertexElement(): NeoVertexFormat.Element {
        //? if >=1.21 {
        //return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.POSITION)
        //? } else {
        return NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_POSITION)
        //? }
    }

    override fun colorVertexElement(): NeoVertexFormat.Element {
        //? if >=1.21 {
        //return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.COLOR)
        //? } else {
        return NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_COLOR)
        //? }
    }

    override fun textureUVVertexElement(): NeoVertexFormat.Element {
        //? if >=1.21 {
        //return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV0)
        //? } else {
        return NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV0)
        //? }
    }

    override fun overlayUVVertexElement(): NeoVertexFormat.Element {
        //? if >=1.21 {
        //return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV1)
        //? } else {
        return NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV1)
        //? }
    }

    override fun lightUVVertexElement(): NeoVertexFormat.Element {
        //? if >=1.21 {
        //return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV2)
        //? } else {
        return NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_UV2)
        //? }
    }

    override fun normalVertexElement(): NeoVertexFormat.Element {
        //? if >=1.21 {
        //return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.NORMAL)
        //? } else {
        return NeoVertexFormatImpl.ElementImpl(DefaultVertexFormat.ELEMENT_NORMAL)
        //? }
    }

    override fun blitScreenVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.BLIT_SCREEN)
    }

    override fun blockVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.BLOCK)
    }

    override fun newEntityVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.NEW_ENTITY)
    }

    override fun particleVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.PARTICLE)
    }

    override fun positionVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION)
    }

    override fun positionColorVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR)
    }

    override fun positionColorNormalVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_NORMAL)
    }

    override fun positionColorLightVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP)
    }

    override fun positionTexVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX)
    }

    override fun positionTexColorVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_COLOR)
    }

    override fun positionColorTexLightVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
    }

    override fun positionTexLightColorVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR)
    }

    override fun positionTexColorNormalVertexFormat(): NeoVertexFormat {
        return NeoVertexFormatImpl(DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL)
    }

    override fun createBufferBuilder(
        size: Int,
        mode: GlBeginMode,
        format: NeoVertexFormat
    ): BufferBuilder {
        //? if >=1.21 {
        /*return BufferBuilder(ByteBufferBuilder(size), when (mode) {
            GlBeginMode.LINES -> VertexFormat.Mode.LINES
            GlBeginMode.LINE_STRIP -> VertexFormat.Mode.LINE_STRIP
            GlBeginMode.TRIANGLES -> VertexFormat.Mode.TRIANGLES
            GlBeginMode.TRIANGLE_STRIP -> VertexFormat.Mode.TRIANGLE_STRIP
            GlBeginMode.TRIANGLE_FAN -> VertexFormat.Mode.TRIANGLE_FAN
            GlBeginMode.QUADS -> VertexFormat.Mode.QUADS
            else -> throw IllegalArgumentException("Minecraft does not support GlBeginMode $mode")
        }, (format as NeoVertexFormatImpl).inner)
        *///? } else {
        throw UnsupportedOperationException("TODO buffer builders") // TODO
        //? }
    }

    override fun <R> dataResultError(message: () -> String): DataResult<R> {
        //? if >=1.19.4 {
        return DataResult.error(message)
        //? } else {
        /*return DataResult.error(message())
        *///? }
    }
}