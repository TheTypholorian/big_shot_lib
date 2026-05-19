package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.DependencyTransformAction
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.commons.Remapper
import kotlin.math.sign

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
        val oldSignature = signature
        var signature = signature
        val interfaceInjections = info.interfaceInjections.get().filter { it.target.get() == name }

        if (interfaceInjections.any { it.typeParams.get().isNotEmpty() } && signature == null) {
            signature = "L$superName;"

            for (injection in interfaceInjections) {
                signature += "L${injection.iface.get()};${interfaces.joinToString(separator = "") { "L$it;" }}"
            }
        }

        if (signature != null) {
            for (injection in interfaceInjections) {
                signature += if (injection.typeParams.get().isEmpty()) {
                    "L${injection.iface.get()};"
                } else {
                    "L${injection.iface.get()}<${injection.typeParams.get().joinToString(separator = "") { "L$it;" }}>;"
                }
            }
        }

        interfaceInjections.forEach {
            if (it.target.get() == name) {
                interfaces.add(remapper.map(it.iface.get()))
            }
        }
Z
        if (interfaceInjections.isNotEmpty()) {
            println("[Big Shot Lib] Injected interfaces ${interfaceInjections.map { it.iface.get() }} to $name, old signature: $oldSignature, new signature: $signature")
        }

        super.visit(version, access, name, signature, superName, interfaces.toTypedArray())
    }
}