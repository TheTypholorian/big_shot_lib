package net.typho.big_shot_lib.intellij.mixin

import com.demonwav.mcdev.util.addAnnotation
import com.demonwav.mcdev.util.referencedMethod
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.LambdaUtil
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiLambdaExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiParameterListOwner
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.PsiTypeParameterList
import com.intellij.psi.PsiTypes
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import net.typho.big_shot_lib.intellij.mixin.MixinUtil.getSelectedPsiElement
import kotlin.jvm.java

class CreateInjectionAction : AnAction() {
    companion object {
        const val CALLBACK_INFO_CLASS = "org.spongepowered.asm.mixin.injection.callback.CallbackInfo"
        const val CALLBACK_INFO_RETURNABLE_CLASS = "org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable"
        const val INJECT_CLASS = "org.spongepowered.asm.mixin.injection.Inject"
        const val AT_CLASS = "org.spongepowered.asm.mixin.injection.At"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val atElement = e.getSelectedPsiElement(PsiMethodCallExpression::class.java) ?: return
        val atMethod = atElement.referencedMethod ?: return
        val injectMethod = PsiTreeUtil.getParentOfType(atElement, PsiParameterListOwner::class.java) ?: return
        val project = e.project ?: return

        val injectMethodName: String
        val injectMethodTypeParameterList: PsiTypeParameterList?
        val injectMethodParameterList: PsiParameterList?
        val injectMethodReturnType: PsiType

        when (injectMethod) {
            is PsiMethod -> {
                injectMethodName = injectMethod.name
                injectMethodTypeParameterList = injectMethod.typeParameterList
                injectMethodParameterList = injectMethod.parameterList
                injectMethodReturnType = injectMethod.returnType ?: PsiTypes.voidType()
            }
            is PsiLambdaExpression -> {
                injectMethodName = MixinUtil.getLambdaMethodName(injectMethod)
                val interfaceMethod = LambdaUtil.getFunctionalInterfaceMethod(injectMethod)
                injectMethodTypeParameterList = interfaceMethod.typeParameterList
                injectMethodParameterList = interfaceMethod.parameterList
                injectMethodReturnType = interfaceMethod.returnType ?: PsiTypes.voidType()
            }
            else -> return
        }

        val mixin = MixinUtil.findOrCreateMixin(
            project,
            GlobalSearchScope.projectScope(project),
            PsiTreeUtil.getParentOfType(atElement, PsiClass::class.java) ?: return
        )

        val styleManager = JavaCodeStyleManager.getInstance(project)

        MixinUtil.addMixinMethod(
            project,
            mixin,
            injectMethodName,
            PsiTypes.voidType()
        ) { factory, method ->
            injectMethodTypeParameterList?.let {
                method.typeParameterList?.replace(it)
            }

            method.parameterList.replace(injectMethodParameterList)

            if (injectMethodReturnType == PsiTypes.voidType()) {
                method.parameterList.add(factory.createParameter("ci", factory.createType(JavaPsiFacade.getInstance(project).findClass(CALLBACK_INFO_CLASS, GlobalSearchScope.allScope(project))!!)))
            } else {
                method.parameterList.add(factory.createParameter("cir", factory.createType(JavaPsiFacade.getInstance(project).findClass(CALLBACK_INFO_RETURNABLE_CLASS, GlobalSearchScope.allScope(project))!!, if (injectMethodReturnType is PsiPrimitiveType) injectMethodReturnType.getBoxedType(injectMethod) else injectMethodReturnType)))
            }

            method.modifierList.setModifierProperty(PsiModifier.PRIVATE, true)

            if (atMethod.modifierList.hasModifierProperty(PsiModifier.STATIC)) {
                method.modifierList.setModifierProperty(PsiModifier.STATIC, true)
            }

            val annotation = factory.createAnnotationFromText("@$INJECT_CLASS(method = \"${MixinUtil.getInjectionTargetMethodDescriptor(injectMethod, injectMethodName, injectMethodReturnType)}\", at = @$AT_CLASS(value = \"INVOKE\", target = \"${MixinUtil.getAtInvokeMethodDescriptor(atMethod)}\"))", mixin)
            method.addAnnotation(annotation)
            styleManager.shortenClassReferences(annotation)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = e.getSelectedPsiElement(PsiMethodCallExpression::class.java)?.referencedMethod != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}