package net.typho.big_shot_lib.api.client.rendering.util

enum class BlockChunkLayer(
    @JvmField
    val settings: NeoRenderType
) {
    SOLID(NeoRenderType.BUILTINS.solid),
    CUTOUT(NeoRenderType.BUILTINS.cutout),
    TRANSLUCENT(NeoRenderType.BUILTINS.translucent),
    TRIPWIRE(NeoRenderType.BUILTINS.tripwire)
}