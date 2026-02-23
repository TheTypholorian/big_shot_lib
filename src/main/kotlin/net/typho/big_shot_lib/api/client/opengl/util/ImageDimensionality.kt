package net.typho.big_shot_lib.api.client.opengl.util

enum class ImageDimensionality(
    @JvmField
    val id: Int
) {
    ONE_D(0),
    TWO_D(1),
    THREE_D(2),
    CUBE(3),
    RECT(4),
    BUFFER(5),
    SUBPASS_DATA(6),
    TILE_IMAGE_DATA_EXT(4173)
}