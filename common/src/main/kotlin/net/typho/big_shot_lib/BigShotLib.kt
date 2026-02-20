package net.typho.big_shot_lib

import com.google.common.collect.Queues
import com.mojang.blaze3d.opengl.GlDevice
import com.mojang.blaze3d.opengl.GlTexture
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.api.client.rendering.util.GlIndexType
import net.typho.big_shot_lib.api.client.rendering.util.GlShapeType
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
    fun VertexFormat.Mode.toNeo(): GlShapeType {
        return when (this) {
            VertexFormat.Mode.LINES -> GlShapeType.LINES
            VertexFormat.Mode.LINE_STRIP -> GlShapeType.LINE_STRIP
            VertexFormat.Mode.DEBUG_LINES -> GlShapeType.DEBUG_LINES
            VertexFormat.Mode.DEBUG_LINE_STRIP -> GlShapeType.DEBUG_LINE_STRIP
            VertexFormat.Mode.TRIANGLES -> GlShapeType.TRIANGLES
            VertexFormat.Mode.TRIANGLE_STRIP -> GlShapeType.TRIANGLE_STRIP
            VertexFormat.Mode.TRIANGLE_FAN -> GlShapeType.TRIANGLE_FAN
            VertexFormat.Mode.QUADS -> GlShapeType.QUADS
        }
    }

    @JvmStatic
    fun GlShapeType.toMojang(): VertexFormat.Mode {
        return when (this) {
            GlShapeType.LINES -> VertexFormat.Mode.LINES
            GlShapeType.LINE_STRIP -> VertexFormat.Mode.LINE_STRIP
            GlShapeType.DEBUG_LINES -> VertexFormat.Mode.DEBUG_LINES
            GlShapeType.DEBUG_LINE_STRIP -> VertexFormat.Mode.DEBUG_LINE_STRIP
            GlShapeType.TRIANGLES -> VertexFormat.Mode.TRIANGLES
            GlShapeType.TRIANGLE_STRIP -> VertexFormat.Mode.TRIANGLE_STRIP
            GlShapeType.TRIANGLE_FAN -> VertexFormat.Mode.TRIANGLE_FAN
            GlShapeType.QUADS -> VertexFormat.Mode.QUADS
        }
    }

    @JvmStatic
    fun VertexFormat.IndexType.toNeo(): GlIndexType {
        return when (this) {
            VertexFormat.IndexType.SHORT -> GlIndexType.USHORT
            VertexFormat.IndexType.INT -> GlIndexType.UINT
        }
    }

    @JvmStatic
    fun GlIndexType.toMojang(): VertexFormat.IndexType? {
        return when (this) {
            GlIndexType.UBYTE -> null
            GlIndexType.USHORT -> VertexFormat.IndexType.SHORT
            GlIndexType.UINT -> VertexFormat.IndexType.INT
        }
    }

    @JvmStatic
    fun init() {
    }
}