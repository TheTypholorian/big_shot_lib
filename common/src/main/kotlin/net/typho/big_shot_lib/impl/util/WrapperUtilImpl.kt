package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.opengl.GlConst
import com.mojang.blaze3d.opengl.GlTexture
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.textures.GpuTexture
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.BigShotLib.glId
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.rendering.buffers.DynamicBufferRegistry
import net.typho.big_shot_lib.api.client.rendering.meshes.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.meshes.NeoVertexFormat
import net.typho.big_shot_lib.api.client.rendering.meshes.TexturedQuad
import net.typho.big_shot_lib.api.client.rendering.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.textures.*
import net.typho.big_shot_lib.api.client.rendering.textures.ClearBit.Companion.initAndGetClearMask
import net.typho.big_shot_lib.api.client.rendering.util.GlIndexType
import net.typho.big_shot_lib.api.client.rendering.util.GlShapeType
import net.typho.big_shot_lib.api.services.NeoResourceManager
import net.typho.big_shot_lib.api.services.WrapperUtil
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.NeoRegistryAccess
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.NeoTagKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.impl.meshes.NeoVertexFormatImpl
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

class WrapperUtilImpl : WrapperUtil {
    companion object {
        @JvmField
        val fboCache = HashMap<RenderTarget, GlFramebuffer>()
        private val textureCache = HashMap<GpuTexture, NeoTexture2D>()

        @JvmStatic
        fun mojangTextureToNeo(texture: GpuTexture): NeoTexture2D {
            return textureCache.computeIfAbsent(texture) { mojTex ->
                val glTexture = mojTex as GlTexture
                val formatId = GlConst.toGlExternalId(mojTex.format)
                return@computeIfAbsent NeoTexture2D(glTexture.glId(), TextureFormat.entries.first { it.internalId == formatId }, false)
            }
        }
    }

    override fun wrap(manager: ResourceManager): NeoResourceManager {
        return object : NeoResourceManager {
            override fun getNamespaces(): MutableSet<String> {
                return manager.namespaces
            }

            override fun getResourceStack(location: ResourceIdentifier): MutableList<Resource> {
                return manager.getResourceStack(location.toMojang())
            }

            override fun listResources(
                folder: String,
                predicate: Predicate<ResourceIdentifier>
            ): MutableMap<ResourceIdentifier, Resource> {
                val map = manager.listResources(folder) { loc -> predicate.test(loc.toNeo()) }
                val rMap = HashMap<ResourceIdentifier, Resource>()

                for (entry in map) {
                    rMap[entry.key.toNeo()] = entry.value
                }

                return rMap
            }

            override fun listResourceStacks(
                folder: String,
                predicate: Predicate<ResourceIdentifier>
            ): MutableMap<ResourceIdentifier, MutableList<Resource>> {
                val map = manager.listResourceStacks(folder) { loc -> predicate.test(loc.toNeo()) }
                val rMap = HashMap<ResourceIdentifier, MutableList<Resource>>()

                for (entry in map) {
                    rMap[entry.key.toNeo()] = entry.value
                }

                return rMap
            }

            override fun listPacks(): Stream<PackResources> {
                return manager.listPacks()
            }

            override fun getResource(location: ResourceIdentifier): Optional<Resource> {
                return manager.getResource(location.toMojang())
            }
        }
    }

    override fun wrap(target: RenderTarget): GlFramebuffer {
        return fboCache.computeIfAbsent(target) {
            object : GlFramebuffer {
                override val colorAttachments: List<GlFramebufferAttachment>
                    get() {
                        val stack = GlStateStack.textures[TextureType.TEXTURE_2D]!!
                        val texId = (target.colorTexture as GlTexture).glId()

                        stack.push(texId)

                        val format = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT)

                        stack.pop()

                        val list = mutableListOf<GlFramebufferAttachment>(
                            NeoTexture2D(
                                texId,
                                TextureFormat.entries.first { it.internalId == format },
                                false
                            )
                        )

                        list.addAll(DynamicBufferRegistry.buffers.toSortedMap().values.toList())

                        return list
                    }
                override val depthAttachment: GlFramebufferAttachment?
                    get() {
                        if (target.depthTexture == null) {
                            return null
                        }

                        val texId = (target.depthTexture as GlTexture).glId()

                        val stack = GlStateStack.textures[TextureType.TEXTURE_2D]!!

                        stack.push(texId)

                        val format = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT)

                        stack.pop()

                        return NeoTexture2D(texId, TextureFormat.entries.first { it.internalId == format }, false)
                    }

                override fun resize(width: Int, height: Int) {
                    target.resize(width, height)
                }

                override fun clear(vararg bits: ClearBit) {
                    OpenGL.INSTANCE.clear(bits.initAndGetClearMask())
                }

                override fun viewport() {
                    OpenGL.INSTANCE.viewport(0, 0, target.viewWidth, target.viewHeight)
                }

                override fun bind(pushStack: Boolean) {
                    if (pushStack) {
                        GlStateStack.framebuffer.push(target.glId())
                    } else {
                        GlStateStack.framebuffer.bind.accept(target.glId())
                    }
                }

                override fun unbind(popStack: Boolean) {
                    if (popStack) {
                        GlStateStack.framebuffer.pop()
                    } else {
                        GlStateStack.framebuffer.bind.accept(0)
                    }
                }

                override fun free() {
                    target.destroyBuffers()
                }

                override fun width() = target.width

                override fun height() = target.height
            }
        }
    }

    override fun <T> wrap(registry: Registry<T>): NeoRegistry<T> {
        return object : NeoRegistry<T> {
            override fun key(): NeoResourceKey<out Registry<T>> {
                return registry.key().toNeo()
            }

            override fun get(value: ResourceIdentifier): T? {
                return registry.get(value.toMojang()).map { it.value() }.getOrNull()
            }

            override fun getKey(value: T): NeoResourceKey<T> {
                return registry.getResourceKey(value!!).orElseThrow().toNeo()
            }

            override fun contains(value: ResourceIdentifier): Boolean {
                return registry.containsKey(value.toMojang())
            }

            override fun keys(): Set<NeoResourceKey<T>> {
                return registry.keySet()
                    .stream()
                    .map { NeoResourceKey<T>(registry.key().location().toNeo(), it.toNeo()) }
                    .collect(Collectors.toSet())
            }

            override fun entries(): Set<Pair<NeoResourceKey<T>, T>> {
                return registry.entrySet()
                    .stream()
                    .map { it.key.toNeo() to it.value }
                    .collect(Collectors.toSet())
            }

            override fun values(): Collection<T> {
                return entries().map { it.second }
            }

            override fun getTag(key: NeoTagKey<T>): Set<T>? {
                return if (registry.listTagIds().anyMatch { it.toNeo() == key }) {
                    registry.getTagOrEmpty(key.toMojang()).toList()
                        .stream()
                        .map { it.value() }
                        .collect(Collectors.toSet())
                } else null
            }

            override fun tags(): Set<NeoTagKey<T>> {
                return registry.listTagIds().map { it.toNeo() }.collect(Collectors.toSet())
            }
        }
    }

    override fun wrap(access: RegistryAccess): NeoRegistryAccess {
        return object : NeoRegistryAccess {
            override fun <T> registry(key: NeoResourceKey<Registry<T>>): NeoRegistry<T>? {
                return access.lookup(key.toMojang()).map { wrap(it) }.getOrNull()
            }
        }
    }

    override fun wrap(quad: BakedQuad): TexturedQuad {
        val list = IntStream.range(0, 4)
            .mapToObj { it * 8 }
            .map {
                Vector3f(
                    Float.fromBits(quad.vertices[it]),
                    Float.fromBits(quad.vertices[it + 1]),
                    Float.fromBits(quad.vertices[it + 2])
                ) to Vector2f(
                    Float.fromBits(quad.vertices[it + 4]),
                    Float.fromBits(quad.vertices[it + 5])
                )
            }
            .toList()

        return TexturedQuad(
            list[0].first,
            list[1].first,
            list[2].first,
            list[3].first,

            list[0].second,
            list[1].second,
            list[2].second,
            list[3].second
        )
    }

    override fun wrap(consumer: VertexConsumer): NeoVertexConsumer {
        return object : NeoVertexConsumer {
            override fun vertex(
                x: Float,
                y: Float,
                z: Float
            ): NeoVertexConsumer {
                consumer.addVertex(x, y, z)
                return this
            }

            override fun color(
                r: Float,
                g: Float,
                b: Float,
                a: Float
            ): NeoVertexConsumer {
                consumer.setColor(r, g, b, a)
                return this
            }

            override fun textureUV(
                u: Float,
                v: Float
            ): NeoVertexConsumer {
                consumer.setUv(u, v)
                return this
            }

            override fun overlayUV(
                u: Int,
                v: Int
            ): NeoVertexConsumer {
                consumer.setUv1(u, v)
                return this
            }

            override fun lightUV(
                u: Int,
                v: Int
            ): NeoVertexConsumer {
                consumer.setUv2(u, v)
                return this
            }

            override fun normal(
                x: Float,
                y: Float,
                z: Float
            ): NeoVertexConsumer {
                consumer.setNormal(x, y, z)
                return this
            }
        }
    }

    override fun createVertexFormatBuilder(): NeoVertexFormat.Builder {
        return NeoVertexFormatImpl.BuilderImpl()
    }

    override fun positionVertexElement(): NeoVertexFormat.Element {
        return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.POSITION)
    }

    override fun colorVertexElement(): NeoVertexFormat.Element {
        return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.COLOR)
    }

    override fun textureUVVertexElement(): NeoVertexFormat.Element {
        return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV0)
    }

    override fun overlayUVVertexElement(): NeoVertexFormat.Element {
        return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV1)
    }

    override fun lightUVVertexElement(): NeoVertexFormat.Element {
        return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.UV2)
    }

    override fun normalVertexElement(): NeoVertexFormat.Element {
        return NeoVertexFormatImpl.ElementImpl(VertexFormatElement.NORMAL)
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

    override fun getIndexType(state: MeshData.DrawState): GlIndexType {
        return state.indexType.toNeo()
    }

    override fun createBufferBuilder(
        buffer: ByteBufferBuilder,
        mode: GlShapeType,
        format: NeoVertexFormat
    ): BufferBuilder {
        return BufferBuilder(buffer, mode.toMojang(), (format as NeoVertexFormatImpl).inner)
    }
}