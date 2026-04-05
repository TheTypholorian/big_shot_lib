package net.typho.big_shot_lib.api.client.rendering.util

enum class BlockChunkLayer(
    @JvmField
    val settings: NeoRenderSettings
) {
    SOLID(NeoRenderSettings.BUILTINS.solid),
    CUTOUT(NeoRenderSettings.BUILTINS.cutout),
    TRANSLUCENT(NeoRenderSettings.BUILTINS.translucent),
    TRIPWIRE(NeoRenderSettings.BUILTINS.tripwire)
}