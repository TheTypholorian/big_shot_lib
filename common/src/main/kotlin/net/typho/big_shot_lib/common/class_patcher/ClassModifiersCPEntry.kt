package net.typho.big_shot_lib.common.class_patcher

import com.google.gson.JsonObject
import org.objectweb.asm.ClassVisitor

class ClassModifiersCPEntry(
    cls: String,
    add: List<Int>,
    remove: List<Int>
) : ModifiersCPEntry(cls, add, remove) {
    override val type = Type

    object Type : ModifiersCPEntry.Type<ClassModifiersCPEntry> {
        override val id: String = "class_modifiers"
        override val cls = ClassModifiersCPEntry::class.java

        override fun createVisitor(
            entries: List<ClassModifiersCPEntry>,
            api: Int,
            parent: ClassVisitor?
        ): ClassVisitor? {
            return if (entries.isEmpty()) null else object : ClassVisitor(api, parent) {
                override fun visit(
                    version: Int,
                    access: Int,
                    name: String,
                    signature: String?,
                    superName: String?,
                    interfaces: Array<String>?
                ) {
                    var access = access

                    for (entry in entries) {
                        if (name == entry.cls) {
                            access = entry.apply(access)
                        }
                    }

                    super.visit(version, access, name, signature, superName, interfaces)
                }
            }
        }

        override fun create(
            cls: String,
            add: List<Int>,
            remove: List<Int>,
            extra: JsonObject
        ): ClassModifiersCPEntry {
            return ClassModifiersCPEntry(cls, add, remove)
        }
    }
}