package net.typho.big_shot_lib.intellij.mixin

import com.demonwav.mcdev.util.addAnnotation
import com.demonwav.mcdev.util.constantStringValue
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.uast.UField
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement

class CreateInvokerAction : AnAction() {
    companion object {
        const val INVOKER_CLASS = "org.spongepowered.asm.mixin.gen.Invoker"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val element = e.getData(CommonDataKeys.PSI_ELEMENT) as? PsiMethod ?: return
        val project = e.project ?: return

        val mixin = MixinUtil.findOrCreateAccessor(
            project,
            GlobalSearchScope.projectScope(project),
            PsiTreeUtil.getParentOfType(element, PsiClass::class.java) ?: return
        )

        MixinUtil.findOrAddMixinMethod(
            project,
            mixin,
            $$"$${project.name}$$${element.name}",
            element.returnTypeElement ?: return
        ) { factory, method ->
            method.parameterList.replace(element.parameterList)

            if (element.modifierList.hasExplicitModifier(PsiModifier.STATIC)) {
                MixinUtil.injectImplementedByMixinException(project, method)
                method.modifierList.setModifierProperty(PsiModifier.STATIC, true)
            } else {
                method.body?.delete()
            }

            val annotation = factory.createAnnotationFromText("@$INVOKER_CLASS(\"${element.name}\")", mixin)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(annotation)
            method.addAnnotation(annotation)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = e.getData(CommonDataKeys.PSI_ELEMENT)?.let { it is PsiMethod && !it.modifierList.hasExplicitModifier(PsiModifier.PUBLIC) } ?: false
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}