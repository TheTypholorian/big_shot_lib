package net.typho.big_shot_lib.api.annotation

annotation class DependencyVersionRange(
    val id: String,
    val min: String = "",
    val max: String = "",
    val mode: Mode = Mode.EXISTS
) {
    enum class Mode {
        EXISTS,
        DOES_NOT_EXIST
    }
}
