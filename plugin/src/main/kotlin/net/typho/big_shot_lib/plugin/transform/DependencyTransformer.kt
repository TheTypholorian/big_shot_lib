package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.DependencyTransformAction
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.commons.Remapper

class DependencyTransformer(
    @JvmField
    val info: DependencyTransformAction.Parameters,
    @JvmField
    val remapper: Remapper,
    api: Int,
    visitor: ClassVisitor
) : ClassVisitor(api, visitor) {
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>?
    ) {
        val interfaces = interfaces?.toMutableList() ?: mutableListOf()

        info.interfaceInjections.get().forEach {
            if (it.target.get() == name) {
                //interfaces.add(remapper.map(it.iface.get()))
            }
        }

        super.visit(version, access, name, signature, superName, interfaces.toTypedArray())
    }
}