package net.typho.big_shot_lib.common.class_patcher

import com.google.gson.JsonObject
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor

class FieldModifiersCPEntry(
    cls: String,
    @JvmField
    val field: String,
    @JvmField
    val desc: String?,
    add: List<Int>,
    remove: List<Int>
) : ModifiersCPEntry(cls, add, remove) {
    override val type = Type

    object Type : ModifiersCPEntry.Type<FieldModifiersCPEntry> {
        override val id: String = "field_modifiers"
        override val cls = FieldModifiersCPEntry::class.java

        override fun createVisitor(
            entries: List<FieldModifiersCPEntry>,
            api: Int,
            parent: ClassVisitor?
        ): ClassVisitor? {
            return if (entries.isEmpty()) null else object : ClassVisitor(api, parent) {
                lateinit var actualEntries: List<FieldModifiersCPEntry>

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

                override fun visitField(
                    access: Int,
                    name: String,
                    descriptor: String,
                    signature: String?,
                    value: Any?
                ): FieldVisitor? {
                    var access = access

                    for (entry in actualEntries) {
                        if (name == entry.field && (entry.desc == null || entry.desc == descriptor)) {
                            access = entry.apply(access)
                        }
                    }

                    return super.visitField(access, name, descriptor, signature, value)
                }
            }
        }

        override fun create(
            cls: String,
            add: List<Int>,
            remove: List<Int>,
            extra: JsonObject
        ): FieldModifiersCPEntry {
            return FieldModifiersCPEntry(cls, extra.getAsJsonPrimitive("method").asString, extra.getAsJsonPrimitive("desc")?.asString, add, remove)
        }
    }
}