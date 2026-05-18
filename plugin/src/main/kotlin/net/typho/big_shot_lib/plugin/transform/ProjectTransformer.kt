package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.transform.util.ClassDesc
import org.gradle.api.model.ObjectFactory
import org.objectweb.asm.ClassVisitor

class ProjectTransformer(
    @JvmField
    val objects: ObjectFactory,
    api: Int,
    visitor: ClassVisitor?
) : ClassVisitor(api, visitor) {
    @JvmField
    var desc: ClassDesc? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String?>?
    ) {
        desc = objects.newInstance(ClassDesc::class.java).also { it.name.set(name) }
        super.visit(version, access, name, signature, superName, interfaces)
    }
}