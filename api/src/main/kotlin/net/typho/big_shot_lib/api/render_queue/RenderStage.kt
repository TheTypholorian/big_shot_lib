package net.typho.big_shot_lib.api.render_queue

enum class RenderStage {
    CLEAR,
    SKY,
    SOLID_TERRAIN,
    ENTITIES,
    BLOCK_ENTITIES,
    DEBUG,
    BLOCK_BREAKING,
    TRANSLUCENT_TERRAIN,
    TRIPWIRE,
    PARTICLES,
    CLOUDS,
    WEATHER,
    END
}