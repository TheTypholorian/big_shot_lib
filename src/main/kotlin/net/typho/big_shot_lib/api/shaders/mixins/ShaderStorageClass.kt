package net.typho.big_shot_lib.api.shaders.mixins

enum class ShaderStorageClass(
    @JvmField
    val id: Int
) {
    UNIFORM_CONSTANT(0),
    INPUT(1),
    UNIFORM(2),
    OUTPUT(3);

    companion object {
        @JvmStatic
        fun get(id: Int): ShaderStorageClass? {
            return entries.firstOrNull { cls -> cls.id == id }
        }
    }
}