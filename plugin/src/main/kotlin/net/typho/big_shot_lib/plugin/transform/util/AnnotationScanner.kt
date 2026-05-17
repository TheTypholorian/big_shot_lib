package net.typho.big_shot_lib.plugin.transform.util

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import kotlin.collections.iterator

@Suppress("UNCHECKED_CAST")
class AnnotationScanner(
    @JvmField
    val annotations: Set<String>
) {
    @JvmField
    val classes = hashMapOf<String, MutableMap<ClassDesc, MutableMap<String, Any?>>>()
    @JvmField
    val methods = hashMapOf<String, MutableMap<MethodDesc, MutableMap<String, Any?>>>()
    @JvmField
    val fields = hashMapOf<String, MutableMap<FieldDesc, MutableMap<String, Any?>>>()

    fun getClasses(annotation: String, out: (cls: ClassDesc, values: ValueSupplier) -> Unit) {
        val info = classes[annotation] ?: return

        for ((cls, values) in info) {
            out(cls, object : ValueSupplier {
                override fun <V> get(scanner: AnnotationField<V>): V? {
                    return values[scanner.name] as? V
                }
            })
        }
    }

    fun getMethods(annotation: String, out: (method: MethodDesc, values: ValueSupplier) -> Unit) {
        val info = methods[annotation] ?: return

        for ((method, values) in info) {
            out(method, object : ValueSupplier {
                override fun <V> get(scanner: AnnotationField<V>): V? {
                    return values[scanner.name] as? V
                }
            })
        }
    }

    fun getFields(annotation: String, out: (method: FieldDesc, values: ValueSupplier) -> Unit) {
        val info = fields[annotation] ?: return

        for ((field, values) in info) {
            out(field, object : ValueSupplier {
                override fun <V> get(scanner: AnnotationField<V>): V? {
                    return values[scanner.name] as? V
                }
            })
        }
    }

    fun createVisitor() = object : ClassVisitor(Opcodes.ASM9) {
        @JvmField
        var desc: ClassDesc? = null
        @JvmField
        val classes = hashMapOf<String, MutableMap<String, Any?>>()

        override fun visit(
            version: Int,
            access: Int,
            name: String,
            signature: String?,
            superName: String?,
            interfaces: Array<String>?
        ) {
            desc = ClassDesc(name)

            super.visit(version, access, name, signature, superName, interfaces)
        }

        override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
            if (annotations.contains(descriptor)) {
                println("Found annotation $descriptor on $desc")
                val values = classes.computeIfAbsent(descriptor) { hashMapOf() }

                return object : AnnotationVisitor(api, super.visitAnnotation(descriptor, visible)) {
                    override fun visit(name: String, value: Any?) {
                        println("\tfound value $name $value")
                        values[name] = value
                    }
                }
            }

            return super.visitAnnotation(descriptor, visible)
        }

        override fun visitEnd() {
            for ((anno, values) in classes) {
                this@AnnotationScanner.classes.computeIfAbsent(anno) { hashMapOf() }[desc!!] = values
            }

            super.visitEnd()
        }
    }

    interface ValueSupplier {
        operator fun <V> get(scanner: AnnotationField<V>): V?
    }
}