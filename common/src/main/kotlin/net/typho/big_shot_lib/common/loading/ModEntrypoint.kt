package net.typho.big_shot_lib.common.loading

data class ModEntrypoint(
    @JvmField
    val parent: String,
    @JvmField
    val type: String,
    @JvmField
    val name: String
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> resolveClass(target: Class<T>): Class<out T> {
        val actual = Class.forName(name)

        if (target.isAssignableFrom(actual)) {
            return actual as Class<out T>
        } else {
            throw ClassCastException("Expected entrypoint $name from mod $parent of type $type to extend $target, but it does not.")
        }
    }
}
