package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.pipeline.RenderTarget
import net.minecraft.client.Minecraft
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.textures.*
import net.typho.big_shot_lib.api.client.rendering.textures.ClearBit.Companion.initAndGetClearMask
import net.typho.big_shot_lib.api.services.ResourceManagerWrapper
import net.typho.big_shot_lib.api.services.WrapperUtil
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream

class WrapperUtilImpl : WrapperUtil {
    override fun wrap(manager: ResourceManager): ResourceManagerWrapper {
        return object : ResourceManagerWrapper {
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
        return object : GlFramebuffer {
            override var colorAttachments: List<GlFramebufferAttachment> = listOf(NeoTexture2D(target.colorTextureId, TextureFormat.RGBA8, false))
                set(value) {
                    throw UnsupportedOperationException()
                }
            override var depthAttachment: GlFramebufferAttachment? = if (target.useDepth) NeoTexture2D(target.depthTextureId, TextureFormat.DEPTH_COMPONENT, false) else null
                set(value) {
                    throw UnsupportedOperationException()
                }

            override fun resize(width: Int, height: Int) {
                target.resize(width, height, Minecraft.ON_OSX)
            }

            override fun clear(vararg bits: ClearBit) {
                OpenGL.INSTANCE.clear(bits.initAndGetClearMask())
            }

            override fun viewport() {
                OpenGL.INSTANCE.viewport(0, 0, target.viewWidth, target.viewHeight)
            }

            override fun bind() {
                target.bindWrite(false)
            }

            override fun unbind() {
                target.unbindWrite()
            }

            override fun free() {
                target.destroyBuffers()
            }

            override fun width() = target.width

            override fun height() = target.height
        }
    }
}