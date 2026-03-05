package net.typho.big_shot_lib.api.client.util.quads

import net.typho.big_shot_lib.api.util.IColor
import org.joml.Vector2fc
import org.joml.Vector2ic
import org.joml.Vector3fc

@JvmRecord
data class BasicVertexData(
    override val pos: Vector3fc,
    override val color: IColor?,
    override val textureUV: Vector2fc?,
    override val overlayUV: Vector2ic?,
    override val lightUV: Vector2ic?,
    override val normal: Vector3fc?
) : NeoVertexData