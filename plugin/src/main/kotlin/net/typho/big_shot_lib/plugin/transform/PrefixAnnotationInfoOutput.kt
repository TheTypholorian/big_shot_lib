package net.typho.big_shot_lib.plugin.transform

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sun.tools.javac.code.TypeAnnotationPosition.field
import groovyjarjarasm.asm.Opcodes
import net.typho.big_shot_lib.plugin.transform.data.FieldDesc
import net.typho.big_shot_lib.plugin.transform.data.MethodDesc
import net.typho.big_shot_lib.plugin.transform.data.PrefixInfo
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor

interface PrefixAnnotationInfoOutput {
    companion object {
        const val FILE_PATH = "META-INF/big_shot_lib/prefix_info.json"
    }

    fun registerSuperclasses(cls: String, superCls: String?, interfaces: Array<String>?)

    fun registerFieldToPrefix(field: FieldDesc, prefix: PrefixInfo)

    fun registerMethodToPrefix(method: MethodDesc, prefix: PrefixInfo)

    open class ToJson : PrefixAnnotationInfoOutput {
        @JvmField
        val json = JsonObject()
        @JvmField
        val superclasses = JsonObject().also { json.add("superclasses", it) }
        @JvmField
        val fields = JsonArray().also { json.add("fields", it) }
        @JvmField
        val methods = JsonArray().also { json.add("methods", it) }

        override fun registerSuperclasses(
            cls: String,
            superCls: String?,
            interfaces: Array<String>?
        ) {
            val json = JsonObject()
            superCls?.let { json.addProperty("superclass", it) }
            interfaces?.let { interfaces -> json.add("interfaces", JsonArray().also { array -> interfaces.forEach { array.add(it) } }) }
            superclasses.add(cls, json)
        }

        override fun registerFieldToPrefix(
            field: FieldDesc,
            prefix: PrefixInfo
        ) {
            val json = JsonObject()

            val fieldJson = JsonObject()
            fieldJson.addProperty("cls", field.cls)
            fieldJson.addProperty("name", field.name)
            fieldJson.addProperty("desc", field.desc)

            json.add("field", fieldJson)
            json.addProperty("prefix", prefix.prefixString)
            json.add("ignoreSubclasses", JsonArray().also { array -> prefix.ignoreSubclasses.forEach { array.add(it.className) } })

            fields.add(json)
        }

        override fun registerMethodToPrefix(
            method: MethodDesc,
            prefix: PrefixInfo
        ) {
            val json = JsonObject()

            val methodJson = JsonObject()
            methodJson.addProperty("cls", method.cls)
            methodJson.addProperty("name", method.name)
            methodJson.addProperty("desc", method.desc)

            json.add("method", methodJson)
            json.addProperty("prefix", prefix.prefixString)
            json.add("ignoreSubclasses", JsonArray().also { array -> prefix.ignoreSubclasses.forEach { array.add(it.className) } })

            methods.add(json)
        }
    }

    open class Visitor @JvmOverloads constructor(
        @JvmField
        val output: PrefixAnnotationInfoOutput,
        api: Int,
        parent: ClassVisitor? = null
    ) : ClassVisitor(api, parent) {
        lateinit var name: String
        @JvmField
        var globalPrefix: PrefixInfo? = null

        override fun visit(
            version: Int,
            access: Int,
            name: String,
            signature: String?,
            superName: String?,
            interfaces: Array<String>?
        ) {
            this.name = name
            output.registerSuperclasses(name, superName, interfaces)

            super.visit(version, access, name, signature, superName, interfaces)
        }

        override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
            if (descriptor == Annotations.PREFIX) {
                return object : PrefixInfo.Visitor(api, super.visitAnnotation(descriptor, visible)) {
                    override fun visitEnd() {
                        super.visitEnd()
                        globalPrefix = get()
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
                if (access and Opcodes.ACC_STATIC == 0) {
                    output.registerFieldToPrefix(desc, it)
                    return super.visitField(access, name, descriptor, signature, value)
                }
            }

            return object : FieldVisitor(api, super.visitField(access, name, descriptor, signature, value)) {
                override fun visitAnnotation(name: String, visible: Boolean): AnnotationVisitor? {
                    if (name == Annotations.PREFIX) {
                        return object : PrefixInfo.Visitor(api, super.visitAnnotation(descriptor, visible)) {
                            override fun visitEnd() {
                                super.visitEnd()
                                output.registerFieldToPrefix(desc, get()!!)
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
                if (access and Opcodes.ACC_STATIC == 0) {
                    output.registerMethodToPrefix(desc, it)
                    return super.visitMethod(access, name, descriptor, signature, exceptions)
                }
            }

            return object : MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                override fun visitAnnotation(name: String, visible: Boolean): AnnotationVisitor? {
                    if (name == Annotations.PREFIX) {
                        return object : PrefixInfo.Visitor(api, super.visitAnnotation(descriptor, visible)) {
                            override fun visitEnd() {
                                super.visitEnd()
                                output.registerMethodToPrefix(desc, get()!!)
                            }
                        }
                    }

                    return super.visitAnnotation(name, visible)
                }
            }
        }
    }
}