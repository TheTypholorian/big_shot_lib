package net.typho.big_shot_lib.api.client.rendering.util

enum class RenderLevelStage {
    AFTER_SKY,
    AFTER_SOLID_BLOCKS,
    AFTER_CUTOUT_MIPPED_BLOCKS,
    AFTER_CUTOUT_BLOCKS,
    AFTER_ENTITIES,
    AFTER_BLOCK_ENTITIES,
    AFTER_TRIPWIRE_BLOCKS,
    AFTER_PARTICLES,
    AFTER_WEATHER,
    AFTER_LEVEL
}