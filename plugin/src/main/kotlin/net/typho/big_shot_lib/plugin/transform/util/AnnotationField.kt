package net.typho.big_shot_lib.plugin.transform.util

data class AnnotationField<V>(
    @JvmField
    val name: String
) {
    companion object {
        @JvmStatic
        val NAMESPACE = "Lnet/typho/big_shot_lib/api/plugin/Namespace;"
        @JvmField
        val NAMESPACE_VALUE = AnnotationField<String>("value")
    }
}
