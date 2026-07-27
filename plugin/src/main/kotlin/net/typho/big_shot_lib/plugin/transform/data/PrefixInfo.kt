package net.typho.big_shot_lib.plugin.transform.data

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Type
import java.io.Serializable

data class PrefixInfo(
    @JvmField
    val prefixString: String,
    @JvmField
    val ignoreSubclasses: List<Type>
) : Serializable {
    open class Visitor @JvmOverloads constructor(
        api: Int,
        parent: AnnotationVisitor? = null
    ) : AnnotationVisitor(api, parent) {
        @JvmField
        var prefix: String? = null
        @JvmField
        var ignoreSubclasses: List<Type> = listOf()

        @Suppress("UNCHECKED_CAST")
        override fun visit(name: String, value: Any?) {
            when (name) {
                "value" -> prefix = value as String
                "ignoreSubclasses" -> ignoreSubclasses = (value as Array<Type>).toList()
            }

            super.visit(name, value)
        }

        fun get() = prefix?.let { PrefixInfo(it, ignoreSubclasses) }
    }
}