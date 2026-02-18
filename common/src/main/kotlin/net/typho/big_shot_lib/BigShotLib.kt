package net.typho.big_shot_lib

import com.google.common.collect.Queues
import com.mojang.blaze3d.opengl.GlDevice
import com.mojang.blaze3d.opengl.GlTexture
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.NeoTagKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.concurrent.ConcurrentLinkedQueue

object BigShotLib {
    @JvmField
    val renderThreadQueue: ConcurrentLinkedQueue<Runnable> = Queues.newConcurrentLinkedQueue()

    @JvmStatic
    fun ResourceLocation.toNeo(): ResourceIdentifier = ResourceIdentifier(namespace, path)

    @JvmStatic
    fun ResourceIdentifier.toMojang(): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)

    @JvmStatic
    fun <T> ResourceKey<T>.toNeo(): NeoResourceKey<T> = NeoResourceKey(registry().toNeo(), location().toNeo())

    @JvmStatic
    fun <T> NeoResourceKey<T>.toMojang(): ResourceKey<T> = ResourceKey.create(ResourceKey.createRegistryKey(registry.toMojang()), location.toMojang())

    @JvmStatic
    fun <T> TagKey<T>.toNeo(): NeoTagKey<T> = NeoTagKey(registry().toNeo().location, location().toNeo())

    @JvmStatic
    fun <T> NeoTagKey<T>.toMojang(): TagKey<T> = TagKey.create(ResourceKey.createRegistryKey(registry.toMojang()), location.toMojang())

    @JvmStatic
    fun RenderTarget.glId(): Int = (colorTexture as GlTexture).getFbo((RenderSystem.getDevice() as GlDevice).directStateAccess(), depthTexture)

    @JvmStatic
    fun init() {
        ShaderMixinManager.register(object : ShaderMixin {
            override fun mixinPostCompile(key: ShaderSourceKey, code: String): String {
                println(key)
                println(code)
                return super.mixinPostCompile(key, code)
            }
        })
    }
}