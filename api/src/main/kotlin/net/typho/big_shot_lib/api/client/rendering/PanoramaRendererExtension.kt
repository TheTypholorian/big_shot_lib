package net.typho.big_shot_lib.api.client.rendering

import net.minecraft.client.renderer.CubeMap
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.plugin.Namespace

@Namespace(BigShotApi.MOD_ID)
interface PanoramaRendererExtension {
    var cubeMap: CubeMap
}