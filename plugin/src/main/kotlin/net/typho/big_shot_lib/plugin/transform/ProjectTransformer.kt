package net.typho.big_shot_lib.plugin.transform

import org.gradle.api.model.ObjectFactory
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
}