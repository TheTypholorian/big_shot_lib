package net.typho.big_shot_lib.common.class_patcher

import com.google.gson.JsonObject
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class MethodModifiersCPEntry(
    cls: String,
    @JvmField
    val method: String,
    @JvmField
    val desc: String?,
    add: List<Int>,
    remove: List<Int>
) : ModifiersCPEntry(cls, add, remove) {
    override val type = Type

    object Type : ModifiersCPEntry.Type<MethodModifiersCPEntry> {
        override val id: String = "method_modifiers"
        override val cls = MethodModifiersCPEntry::class.java

        override fun createVisitor(
            entries: List<MethodModifiersCPEntry>,
            api: Int,
            parent: ClassVisitor?
        ): ClassVisitor? {
            return if (entries.isEmpty()) null else object : ClassVisitor(api, parent) {
                lateinit var actualEntries: List<MethodModifiersCPEntry>

                override fun visit(
                    version: Int,
                    access: Int,
                    name: String,
                    signature: String?,
                    superName: String?,
                    interfaces: Array<String>?
                ) {
                    actualEntries = entries.filter { it.cls == name }
                    super.visit(version, access, name, signature, superName, interfaces)
                }

                override fun visitMethod(
                    access: Int,
                    name: String,
                    descriptor: String,
                    signature: String?,
                    exceptions: Array<String>?
                ): MethodVisitor? {
                    var access = access

                    for (entry in actualEntries) {
                        if (name == entry.method && (entry.desc == null || entry.desc == descriptor)) {
                            access = entry.apply(access)
                        }
                    }

                    return super.visitMethod(access, name, descriptor, signature, exceptions)
                }
            }
        }

        override fun create(
            cls: String,
            add: List<Int>,
            remove: List<Int>,
            extra: JsonObject
        ): MethodModifiersCPEntry {
            return MethodModifiersCPEntry(cls, extra.getAsJsonPrimitive("method").asString, extra.getAsJsonPrimitive("desc")?.asString, add, remove)
        }
    }
}