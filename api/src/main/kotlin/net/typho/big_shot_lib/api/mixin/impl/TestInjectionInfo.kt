package net.typho.big_shot_lib.api.mixin.impl

import net.typho.big_shot_lib.api.mixin.Test
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.MethodNode
import org.spongepowered.asm.mixin.injection.code.Injector
import org.spongepowered.asm.mixin.injection.invoke.InvokeInjector
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes
import org.spongepowered.asm.mixin.injection.struct.Target
import org.spongepowered.asm.mixin.transformer.MixinTargetContext

@InjectionInfo.AnnotationType(Test::class)
class TestInjectionInfo : InjectionInfo {
    constructor(mixin: MixinTargetContext?, method: MethodNode?, annotation: AnnotationNode?) : super(
        mixin,
        method,
        annotation
    )

    constructor(mixin: MixinTargetContext?, method: MethodNode?, annotation: AnnotationNode?, atKey: String?) : super(
        mixin,
        method,
        annotation,
        atKey
    )

    override fun parseInjector(injectAnnotation: AnnotationNode?): Injector {
        return object : InvokeInjector(this, "@Test") {
            override fun injectAtInvoke(
                target: Target,
                node: InjectionNodes.InjectionNode
            ) {
                /*
                val method = node.currentTarget as MethodInsnNode
                val insns = InsnList()
                invokeHandler(insns)
                target.insns.insertBefore(method, insns)
                 */
            }
        }
    }

    override fun getDescription(): String {
        return "Test injector from big shot lib"
    }
}