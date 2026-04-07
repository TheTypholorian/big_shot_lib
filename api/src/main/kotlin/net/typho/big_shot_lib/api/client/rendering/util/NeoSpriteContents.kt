package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.client.renderer.texture.SpriteTicker
import net.minecraft.server.packs.resources.ResourceMetadata
import net.typho.big_shot_lib.api.util.resource.NamedResource
import org.lwjgl.system.NativeResource

interface NeoSpriteContents : NamedResource, NativeResource {
    val width: Int
    val height: Int
    val metadata: ResourceMetadata

    fun createTicker(): SpriteTicker?

    fun uploadFirstFrame(x: Int, y: Int)
}