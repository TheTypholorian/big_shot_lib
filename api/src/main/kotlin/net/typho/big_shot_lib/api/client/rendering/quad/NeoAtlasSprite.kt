package net.typho.big_shot_lib.api.client.rendering.quad

import net.typho.big_shot_lib.api.util.resource.NamedResource
import org.joml.Vector2f

interface NeoAtlasSprite : NamedResource {
    val atlas: NeoAtlas
    val x: Int
    val y: Int
    val width: Int
    val height: Int
    val u0: Float
    val u1: Float
    val v0: Float
    val v1: Float
    val uv0: Vector2f
        get() = Vector2f(u0, v0)
    val uv1: Vector2f
        get() = Vector2f(u1, v1)
}