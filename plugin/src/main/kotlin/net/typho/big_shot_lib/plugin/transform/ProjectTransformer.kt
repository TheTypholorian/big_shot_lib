package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.ModLoader
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.gradle.api.model.ObjectFactory
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor

class ProjectTransformer(
    @JvmField
    val objects: ObjectFactory,
    api: Int,
    visitor: ClassVisitor?
) : ClassVisitor(api, visitor) {
    @JvmField
    var desc: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String?>?
    ) {
        desc = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        if (descriptor == Annotations.ONLY_IN) {
            return object : AnnotationVisitor(api) {
                var client = false

                override fun visitEnum(name: String, descriptor: String, value: String) {
                    if (name == "value" && value == "CLIENT") {
                        client = true
                    }
                }

                override fun visitEnd() {
                    ModLoader.CURRENT.mapOnlyInAnnotation(this@ProjectTransformer, client)
                }
            }
        } else {
            return super.visitAnnotation(descriptor, visible)
        }
    }
}