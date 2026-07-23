package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.transform.data.ClassRename
import net.typho.big_shot_lib.plugin.transform.data.FieldRename
import net.typho.big_shot_lib.plugin.transform.data.MethodRename
import org.objectweb.asm.commons.Remapper

class ToCompileRemapper(
    @JvmField
    val classRenames: List<ClassRename>,
    @JvmField
    val methodRenames: List<MethodRename>,
    @JvmField
    val fieldRenames: List<FieldRename>,
    @JvmField
    val markChanged: Runnable,
    api: Int
) : Remapper(api) {
    constructor(
        parameters: NeoTransformParameters,
        markChanged: Runnable,
        api: Int
    ) : this(parameters.classRenames.get(), parameters.methodRenames.get(), parameters.fieldRenames.get(), markChanged, api)

    override fun map(internalName: String): String {
        var internalName = internalName
        val index = internalName.lastIndexOf('$')

        if (index != -1) {
            val parent = map(internalName.substring(0, index))
            internalName = "$parent${internalName.substring(index)}"
        }

        return classRenames.lastOrNull { it.from == internalName }?.to?.also { markChanged.run() } ?: internalName
    }

    override fun mapMethodName(owner: String, name: String, descriptor: String?): String {
        if (name == "<init>" || name == "<clinit>") {
            return name
        }

        val owner = map(owner)
        val descriptor = descriptor?.let { mapMethodDesc(it) }

        return methodRenames.lastOrNull { it.from.let { it.cls == owner && it.name == name && (descriptor == null || it.desc == descriptor) } }?.to?.also { markChanged.run() } ?: name
    }

    override fun mapFieldName(owner: String, name: String, descriptor: String): String {
        val owner = map(owner)
        val descriptor = mapDesc(descriptor)

        return fieldRenames.lastOrNull { it.from.let { it.cls == owner && it.name == name && it.desc == descriptor } }?.to?.also { markChanged.run() } ?: name
    }
}