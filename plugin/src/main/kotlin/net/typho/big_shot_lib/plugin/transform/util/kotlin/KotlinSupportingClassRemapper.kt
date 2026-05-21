package net.typho.big_shot_lib.plugin.transform.util.kotlin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper
import kotlin.metadata.KmClassifier
import kotlin.metadata.KmType
import kotlin.metadata.jvm.KotlinClassMetadata
import kotlin.metadata.jvm.signature

class KotlinSupportingClassRemapper(
    api: Int,
    visitor: ClassVisitor,
    remapper: Remapper
) : ClassRemapper(api, visitor, remapper) {
    fun remapKtType(type: KmType) {
        val cls = type.classifier

        if (cls is KmClassifier.Class) {
            type.classifier = KmClassifier.Class(
                remapper.mapType(cls.name)
            )
        }

        for (arg in type.arguments) {
            arg.type?.let {
                remapKtType(it)
            }
        }

        type.outerType?.let {
            remapKtType(it)
        }

        type.abbreviatedType?.let {
            remapKtType(it)
        }
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        return if (descriptor == "Lkotlin/Metadata;") {
            KotlinMetadataVisitor(
                api,
                { super.visitAnnotation(descriptor, visible) },
                { metadata ->
                    val classMetadata = KotlinClassMetadata.readStrict(metadata)

                    if (classMetadata is KotlinClassMetadata.Class) {
                        val owner = remapper.mapType(classMetadata.kmClass.name.replace('.', '$'))
                        classMetadata.kmClass.name = owner.replace('$', '.')

                        for (function in classMetadata.kmClass.functions) {
                            function.signature?.let { function.name = remapper.mapMethodName(owner, function.name, it.descriptor) }
                            remapKtType(function.returnType)
                            function.valueParameters.forEach { remapKtType(it.type) }
                        }

                        classMetadata.kmClass.nestedClasses.replaceAll { remapper.mapType(it) }
                    }

                    classMetadata.write()
                }
            )
        } else {
            super.visitAnnotation(descriptor, visible)
        }
    }
}