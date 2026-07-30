package net.typho.big_shot_lib.api.agent

import net.typho.big_shot_lib.common.annotation.Environment
import net.typho.big_shot_lib.common.annotation.OnlyIn
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TypeInsnNode

object OnlyInProcessor {
    @JvmStatic
    fun injectInvalidEnvironmentException(
        node: MethodNode,
        name: String,
        targetEnv: Environment,
        currentEnv: Environment
    ) {
        val insn = InsnList()
        insn.add(TypeInsnNode(Opcodes.NEW, "net/typho/big_shot_lib/api/error/InvalidEnvironmentException"))
        insn.add(InsnNode(Opcodes.DUP))
        insn.add(LdcInsnNode("Tried to load $name on ${currentEnv.name.lowercase()} but it is ${targetEnv.name.lowercase()}-side only"))
        insn.add(MethodInsnNode(
            Opcodes.INVOKESPECIAL,
            "net/typho/big_shot_lib/api/error/InvalidEnvironmentException",
            "<init>",
            "(Ljava/lang/String;)V",
            false
        ))
        insn.add(InsnNode(Opcodes.ATHROW))
        node.instructions.insert(insn)
        node.maxStack = node.maxStack.coerceAtLeast(3)
    }

    @JvmStatic
    fun process(
        currentEnv: Environment,
        node: ClassNode,
        info: ClassWriterInfo
    ) {
        node.visibleAnnotations?.find { it.desc == Type.getDescriptor(OnlyIn::class.java) }?.let { onlyIn ->
            lateinit var targetEnv: Environment
            val iterator = onlyIn.values.iterator()

            while (iterator.hasNext()) {
                val name = iterator.next() as String
                val value = iterator.next()

                if (name == "value") {
                    targetEnv = Environment.valueOf((value as Array<*>)[1] as String)
                    break
                }
            }

            if (currentEnv != targetEnv) {
                val clinit = node.methods.find { it.name == "<clinit>" } ?: MethodNode(
                    Opcodes.ACC_STATIC,
                    "<clinit>",
                    "()V",
                    null,
                    null
                ).also { node.methods.add(it) }
                injectInvalidEnvironmentException(
                    clinit,
                    "class ${node.name}",
                    targetEnv,
                    currentEnv
                )
                info.markChanged()
                info.computeFrames()
            }
        }

        for (method in node.methods) {
            method.visibleAnnotations?.find { it.desc == Type.getDescriptor(OnlyIn::class.java) }?.let { onlyIn ->
                lateinit var targetEnv: Environment
                val iterator = onlyIn.values.iterator()

                while (iterator.hasNext()) {
                    val name = iterator.next() as String
                    val value = iterator.next()

                    if (name == "value") {
                        targetEnv = Environment.valueOf((value as Array<*>)[1] as String)
                        break
                    }
                }

                if (currentEnv != targetEnv) {
                    injectInvalidEnvironmentException(
                        method,
                        "method ${method.name} ${method.desc}",
                        targetEnv,
                        currentEnv
                    )
                    info.markChanged()
                    info.computeFrames()
                }
            }
        }
    }
}