package net.typho.big_shot_lib.intellij.mixin

import com.demonwav.mcdev.util.addAnnotation
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiField
import com.intellij.psi.PsiModifier
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.uast.UField
import org.jetbrains.uast.toUElement

class CreateAccessorAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val element = e.getData(CommonDataKeys.PSI_ELEMENT) as? PsiField ?: return
        val target = PsiTreeUtil.getParentOfType(element, PsiClass::class.java) ?: return

        val project = e.project ?: return
        val scope = GlobalSearchScope.projectScope(project)

        val mixins = MixinUtil.getAccessors(project, scope, target)
        val mixin = when (mixins.size) {
            0 -> TODO("make mixin")
            1 -> mixins[0]
            else -> TODO("prompt for which mixin")
        }

        WriteCommandAction.runWriteCommandAction(project) {
            val factory = PsiElementFactory.getInstance(project)

            val method = factory.createMethod(
                $$"$${project.name}$get$${element.name[0].uppercase()}$${element.name.substring(1)}",
                element.type
            )
            method.body?.delete()
            val annotation = factory.createAnnotationFromText("@org.spongepowered.asm.mixin.gen.Accessor(\"${element.name}\")", mixin)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(annotation)
            method.addAnnotation(annotation)

            mixin.add(method)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = e.getData(CommonDataKeys.PSI_ELEMENT).toUElement()?.let { it is UField && (it.modifierList?.hasExplicitModifier(PsiModifier.PUBLIC) != true || it.modifierList?.hasExplicitModifier(PsiModifier.FINAL) == true)} ?: false
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}