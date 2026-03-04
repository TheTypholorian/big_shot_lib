package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.client.util.panoramas.PanoramaSet

interface PanoramaFactory {
    fun register(panorama: PanoramaSet)
}