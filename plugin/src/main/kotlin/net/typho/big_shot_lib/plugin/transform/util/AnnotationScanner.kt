package net.typho.big_shot_lib.plugin.transform.util

import org.gradle.api.model.ObjectFactory
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import kotlin.collections.iterator

@Suppress("UNCHECKED_CAST")
class AnnotationScanner(
    @JvmField
    val objects: ObjectFactory,
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
            desc = objects.newInstance(ClassDesc::class.java).also { it.name.set(name) }

            super.visit(version, access, name, signature, superName, interfaces)
        }

        override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
            if (annotations.contains(descriptor)) {
                val values = classes.computeIfAbsent(descriptor) { hashMapOf() }

                return object : AnnotationVisitor(api, super.visitAnnotation(descriptor, visible)) {
                    override fun visit(name: String, value: Any?) {
                        values[name] = value
                    }
                }
            }

            return super.visitAnnotation(descriptor, visible)
        }

        override fun visitMethod(
            access: Int,
            name: String,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String?>?
        ): MethodVisitor {
            val desc = objects.newInstance(MethodDesc::class.java)
            desc.cls.set(this.desc!!.name)
            desc.name.set(name)
            desc.desc.set(descriptor)

            return object : MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                @JvmField
                val methods = hashMapOf<String, MutableMap<String, Any?>>()

                override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
                    if (annotations.contains(descriptor)) {
                        val values = methods.computeIfAbsent(descriptor) { hashMapOf() }

                        return object : AnnotationVisitor(api, super.visitAnnotation(descriptor, visible)) {
                            override fun visit(name: String, value: Any?) {
                                values[name] = value
                            }
                        }
                    }

                    return super.visitAnnotation(descriptor, visible)
                }

                override fun visitEnd() {
                    for ((anno, values) in methods) {
                        this@AnnotationScanner.methods.computeIfAbsent(anno) { hashMapOf() }[desc] = values
                    }

                    super.visitEnd()
                }
            }
        }

        override fun visitField(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            value: Any?
        ): FieldVisitor {
            val desc = objects.newInstance(FieldDesc::class.java)
            desc.cls.set(this.desc!!.name)
            desc.name.set(name)
            desc.desc.set(descriptor)

            return object : FieldVisitor(api, super.visitField(access, name, descriptor, signature, value)) {
                @JvmField
                val fields = hashMapOf<String, MutableMap<String, Any?>>()

                override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
                    if (annotations.contains(descriptor)) {
                        val values = fields.computeIfAbsent(descriptor) { hashMapOf() }

                        return object : AnnotationVisitor(api, super.visitAnnotation(descriptor, visible)) {
                            override fun visit(name: String, value: Any?) {
                                values[name] = value
                            }
                        }
                    }

                    return super.visitAnnotation(descriptor, visible)
                }

                override fun visitEnd() {
                    for ((anno, values) in fields) {
                        this@AnnotationScanner.fields.computeIfAbsent(anno) { hashMapOf() }[desc] = values
                    }

                    super.visitEnd()
                }
            }
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