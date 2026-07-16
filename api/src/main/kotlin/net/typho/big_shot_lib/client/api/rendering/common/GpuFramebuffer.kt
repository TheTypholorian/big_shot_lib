package net.typho.big_shot_lib.client.api.rendering.common

import net.typho.big_shot_lib.api.util.Extension

interface GpuFramebuffer : Extension<GpuFramebuffer>, GpuResource {
    var colorTexture: GpuTexture?
    var depthTexture: GpuTexture?
    override val type: GpuResourceType
        get() = GpuResourceType.FRAMEBUFFER
}