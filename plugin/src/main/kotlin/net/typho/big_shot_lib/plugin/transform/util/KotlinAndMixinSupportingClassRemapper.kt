package net.typho.big_shot_lib.plugin.transform.util

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper
import kotlin.metadata.KmClassifier
import kotlin.metadata.KmType
import kotlin.metadata.jvm.KotlinClassMetadata
import kotlin.metadata.jvm.signature

class KotlinAndMixinSupportingClassRemapper(
    api: Int,
    visitor: ClassVisitor,
    remapper: Remapper
) : ClassRemapper(api, visitor, remapper) {
    fun mapKtType(type: KmType) {
        val cls = type.classifier

        if (cls is KmClassifier.Class) {
            type.classifier = KmClassifier.Class(
                remapper.mapType(cls.name)
            )
        }

        for (arg in type.arguments) {
            arg.type?.let {
                mapKtType(it)
            }
        }

        type.outerType?.let {
            mapKtType(it)
        }

        type.abbreviatedType?.let {
            mapKtType(it)
        }
    }

    private var name: String? = null
    private var target: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String?>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.name = name
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        return if (descriptor == "Lkotlin/Metadata;") {
            KotlinMetadataVisitor(
                api,
                { super.visitAnnotation(descriptor, visible) },
                { metadata ->
                    val classMetadata = KotlinClassMetadata.readStrict(metadata)

                    if (classMetadata is KotlinClassMetadata.Class) { // TODO unfuck
                        val owner = remapper.mapType(classMetadata.kmClass.name.replace('.', '$'))
                        classMetadata.kmClass.name = owner.replace('$', '.')

                        for (function in classMetadata.kmClass.functions) {
                            function.signature?.let { function.name = remapper.mapMethodName(owner, function.name, it.descriptor) }
                            mapKtType(function.returnType)
                            function.valueParameters.forEach { mapKtType(it.type) }
                        }

                        classMetadata.kmClass.nestedClasses.replaceAll { remapper.mapType(it) }
                    }

                    classMetadata.write()
                }
            )
        } else if (descriptor == "Lorg/spongepowered/asm/mixin/Mixin;") {
            MixinTargetRemapper(api, super.visitAnnotation(descriptor, visible), remapper, name!!) { target = it }
        } else {
            super.visitAnnotation(descriptor, visible)
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String?>?
    ): MethodVisitor {
        return object : MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
            override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
                return if (descriptor.contains("mixin")) {
                    MixinInjectionRemapper(api, super.visitAnnotation(descriptor, visible), remapper, target!!)
                } else {
                    super.visitAnnotation(descriptor, visible)
                }
            }
        }
    }
}