package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.system.NativeResource

interface GlResource : GlNamed, NativeResource {
    val type: GlResourceType
    val freed: Boolean
}