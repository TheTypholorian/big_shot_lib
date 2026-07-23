package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.transform.data.FieldDesc
import net.typho.big_shot_lib.plugin.transform.data.MethodDesc
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor

interface PrefixAnnotationInfoOutput {
    fun registerSuperclasses(cls: String, superCls: String?, interfaces: Array<String>?)

    fun registerFieldToPrefix(method: FieldDesc, prefix: String)

    fun registerMethodToPrefix(method: MethodDesc, prefix: String)

    fun createVisitor(api: Int, parent: ClassVisitor?) = object : ClassVisitor(api, parent) {
        lateinit var name: String
        @JvmField
        var globalPrefix: String? = null

        override fun visit(
            version: Int,
            access: Int,
            name: String,
            signature: String?,
            superName: String?,
            interfaces: Array<String>?
        ) {
            this.name = name
            registerSuperclasses(name, superName, interfaces)

            super.visit(version, access, name, signature, superName, interfaces)
        }

        override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
            if (descriptor == Annotations.PREFIX) {
                return object : AnnotationVisitor(api, super.visitAnnotation(descriptor, visible)) {
                    override fun visit(name: String, value: Any?) {
                        if (name == "value") {
                            globalPrefix = value as String
                        }

                        super.visit(name, value)
                    }
                }
            }

            return super.visitAnnotation(descriptor, visible)
        }

        override fun visitField(
            access: Int,
            name: String,
            descriptor: String,
            signature: String?,
            value: Any?
        ): FieldVisitor? {
            val desc = FieldDesc(this.name, name, descriptor)

            globalPrefix?.let {
                registerFieldToPrefix(desc, it)
                return super.visitField(access, name, descriptor, signature, value)
            }

            return object : FieldVisitor(api, super.visitField(access, name, descriptor, signature, value)) {
                override fun visitAnnotation(name: String, visible: Boolean): AnnotationVisitor? {
                    if (name == Annotations.PREFIX) {
                        return object : AnnotationVisitor(api, super.visitAnnotation(name, visible)) {
                            override fun visit(name: String, value: Any?) {
                                if (name == "value") {
                                    registerFieldToPrefix(desc, value as String)
                                }

                                super.visit(name, value)
                            }
                        }
                    }

                    return super.visitAnnotation(name, visible)
                }
            }
        }

        override fun visitMethod(
            access: Int,
            name: String,
            descriptor: String,
            signature: String?,
            exceptions: Array<out String?>?
        ): MethodVisitor? {
            val desc = MethodDesc(this.name, name, descriptor)

            globalPrefix?.let {
                registerMethodToPrefix(desc, it)
                return super.visitMethod(access, name, descriptor, signature, exceptions)
            }

            return object : MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                override fun visitAnnotation(name: String, visible: Boolean): AnnotationVisitor? {
                    if (name == Annotations.PREFIX) {
                        return object : AnnotationVisitor(api, super.visitAnnotation(name, visible)) {
                            override fun visit(name: String, value: Any?) {
                                if (name == "value") {
                                    registerMethodToPrefix(desc, value as String)
                                }

                                super.visit(name, value)
                            }
                        }
                    }

                    return super.visitAnnotation(name, visible)
                }
            }
        }
    }
}