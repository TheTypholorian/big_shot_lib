package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.transform.util.ClassDesc
import org.objectweb.asm.ClassVisitor

class ProjectTransformer(
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
        desc = ClassDesc(name)
        super.visit(version, access, name, signature, superName, interfaces)
    }
}