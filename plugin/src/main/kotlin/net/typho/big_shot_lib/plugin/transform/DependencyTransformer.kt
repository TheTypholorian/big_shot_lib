package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.*
import net.typho.big_shot_lib.plugin.DependencyTransformAction
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.signature.SignatureReader
import org.objectweb.asm.signature.SignatureVisitor
import org.objectweb.asm.signature.SignatureWriter

class DependencyTransformer(
    @JvmField
    val info: DependencyTransformAction.Parameters,
    @JvmField
    val overloads: (newDesc: String, oldDesc: String, argumentConverters: List<ArgumentOverloadConverter?>) -> Unit,
    @JvmField
    val remapper: Remapper,
    api: Int,
    visitor: ClassVisitor
) : ClassVisitor(api, visitor) {
    @JvmField
    var name: String? = null
    @JvmField
    var isInterface = false

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>?
    ) {
        this.name = name
        isInterface = access and Opcodes.ACC_INTERFACE != 0

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

        if (interfaceInjections.isNotEmpty()) {
            println("[Big Shot Lib] Injected interfaces ${interfaceInjections.map { it.iface.get() }} to $name, old signature: $oldSignature, new signature: $signature")
        }

        for (injection in info.staticMethodInjections.get()) {
            if (injection.targetClass.get() == name) {
                val method = super.visitMethod(
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                    injection.targetMethodName.get(),
                    injection.redirectTo.get().desc.get(),
                    injection.signature.orNull,
                    injection.exceptions.get().toTypedArray()
                )

                val args = Type.getArgumentTypes(injection.redirectTo.get().desc.get())
                val ret = Type.getReturnType(injection.redirectTo.get().desc.get())

                var slot = 0

                for (arg in args) {
                    method.visitVarInsn(arg.getOpcode(Opcodes.ILOAD), slot)
                    slot += arg.size
                }

                method.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    injection.redirectTo.get().cls.get(),
                    injection.redirectTo.get().name.get(),
                    injection.redirectTo.get().desc.get(),
                    false
                )

                method.visitInsn(ret.getOpcode(Opcodes.IRETURN))

                method.visitMaxs(0, 0)
                method.visitEnd()
            }
        }

        super.visit(version, access, name, signature, superName, interfaces.toTypedArray())
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        if (access and Opcodes.ACC_SYNTHETIC == 0) {
            val args = Type.getArgumentTypes(descriptor)
            val ret = Type.getReturnType(descriptor)
            val argumentConverterCache = hashMapOf<Type, List<ArgumentOverloadConverter>>()

            fun getConverter(type: Type) = argumentConverterCache.computeIfAbsent(type) { key ->
                info.argumentOverloadConverters.get().filter { it.to.get() == type.internalName }
            }

            val permutationArray = args.map { getConverter(it).size }.toIntArray()
            val temp = IntArray(permutationArray.size) { 0 }

            fun permutate() {
                if (temp.sum() > 0) {
                    val newArgConverters = temp.mapIndexed { index, i -> if (i == 0) null else argumentConverterCache[args[index]]!![i - 1] }
                    val newTypes = temp.mapIndexed { index, i -> if (i == 0) args[index] else Type.getType("L${argumentConverterCache[args[index]]!![i - 1].from.get()};") }
                    val desc = Type.getMethodDescriptor(ret, *newTypes.toTypedArray())
                    overloads(desc, descriptor, newArgConverters)
                    var signature = signature

                    if (signature != null) {
                        val writer = SignatureWriter()
                        var parameter = -1
                        var skip = false

                        // ASM WHY DO YOU NOT FOLLOW THE PATTERN FOR SIGNATURES THAT YOU DO FOR EVERY OTHER TYPE OF VISITOR (a forward argument to another visitor)
                        SignatureReader(signature).accept(object : SignatureVisitor(Opcodes.ASM9) {
                            override fun visitFormalTypeParameter(name: String) {
                                writer.visitFormalTypeParameter(name)
                            }

                            override fun visitClassBound(): SignatureVisitor {
                                return writer.visitClassBound()
                            }

                            override fun visitInterfaceBound(): SignatureVisitor {
                                return writer.visitInterfaceBound()
                            }

                            override fun visitParameterType(): SignatureVisitor {
                                parameter++
                                skip = temp[parameter] != 0

                                writer.visitParameterType()

                                if (skip) {
                                    val type = newTypes[parameter]

                                    when (type.sort) {
                                        Type.OBJECT -> {
                                            writer.visitClassType(type.internalName)
                                            writer.visitEnd()
                                        }

                                        Type.ARRAY -> {
                                            repeat(type.dimensions) {
                                                writer.visitArrayType()
                                            }

                                            val element = type.elementType

                                            if (element.sort == Type.OBJECT) {
                                                writer.visitClassType(element.internalName)
                                                writer.visitEnd()
                                            } else {
                                                writer.visitBaseType(element.descriptor[0])
                                            }
                                        }

                                        else -> {
                                            writer.visitBaseType(type.descriptor[0])
                                        }
                                    }

                                    return object : SignatureVisitor(Opcodes.ASM9) {}
                                }

                                return this
                            }

                            override fun visitReturnType(): SignatureVisitor {
                                skip = false
                                writer.visitReturnType()
                                return this
                            }

                            override fun visitExceptionType(): SignatureVisitor {
                                skip = false
                                writer.visitExceptionType()
                                return this
                            }

                            override fun visitBaseType(descriptor: Char) {
                                if (!skip) {
                                    writer.visitBaseType(descriptor)
                                }
                            }

                            override fun visitTypeVariable(name: String) {
                                if (!skip) {
                                    writer.visitTypeVariable(name)
                                }
                            }

                            override fun visitArrayType(): SignatureVisitor {
                                return if (skip) {
                                    object : SignatureVisitor(Opcodes.ASM9) {}
                                } else {
                                    writer.visitArrayType()
                                    this
                                }
                            }

                            override fun visitClassType(name: String) {
                                if (!skip) {
                                    writer.visitClassType(name)
                                }
                            }

                            override fun visitInnerClassType(name: String) {
                                if (!skip) {
                                    writer.visitInnerClassType(name)
                                }
                            }

                            override fun visitTypeArgument() {
                                if (!skip) {
                                    writer.visitTypeArgument()
                                }
                            }

                            override fun visitTypeArgument(wildcard: Char): SignatureVisitor {
                                return if (skip) {
                                    object : SignatureVisitor(Opcodes.ASM9) {}
                                } else {
                                    writer.visitTypeArgument(wildcard)
                                    this
                                }
                            }

                            override fun visitEnd() {
                                if (!skip) {
                                    writer.visitEnd()
                                }
                            }
                        })

                        signature = writer.toString()
                    }

                    val method = super.visitMethod(
                        if (isInterface) {
                            access and Opcodes.ACC_ABSTRACT.inv() and Opcodes.ACC_NATIVE.inv()
                        } else {
                            access
                        },
                        name,
                        desc,
                        signature,
                        exceptions
                    )

                    method.visitCode()

                    val static = access and Opcodes.ACC_STATIC != 0

                    if (!static) {
                        method.visitVarInsn(Opcodes.ALOAD, 0)
                    }

                    var slot = if (static) 0 else 1

                    args.forEachIndexed { index, arg ->
                        val converter = newArgConverters[index]

                        if (converter == null) {
                            method.visitVarInsn(arg.getOpcode(Opcodes.ILOAD), slot)
                        } else {
                            method.visitVarInsn(Type.getObjectType(converter.from.get()).getOpcode(Opcodes.ILOAD), slot)
                            method.visitMethodInsn(
                                Opcodes.INVOKESTATIC,
                                converter.converter.get().cls.get(),
                                converter.converter.get().name.get(),
                                converter.converter.get().desc.get(),
                                false
                            )
                        }

                        slot += args[index].size
                    }

                    method.visitMethodInsn(
                        if (static) Opcodes.INVOKESTATIC else Opcodes.INVOKEVIRTUAL,
                        this.name!!,
                        name,
                        descriptor,
                        false
                    )

                    method.visitInsn(ret.getOpcode(Opcodes.IRETURN))
                    method.visitMaxs(0, 0)
                    method.visitEnd()
                }
            }

            fun step(index: Int) {
                if (index == temp.size) {
                    permutate()
                } else {
                    for (num in 0..permutationArray[index]) {
                        temp[index] = num
                        step(index + 1)
                    }
                }
            }

            step(0)
        }

        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        return info.loader.get().unmapOnlyInAnnotation(this, descriptor, api) ?: super.visitAnnotation(descriptor, visible)
    }
}