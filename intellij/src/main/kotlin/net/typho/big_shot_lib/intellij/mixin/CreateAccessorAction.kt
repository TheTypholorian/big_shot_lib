package net.typho.big_shot_lib.intellij.mixin

import com.demonwav.mcdev.util.addAnnotation
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiModifier
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import net.typho.big_shot_lib.intellij.mixin.MixinUtil.getSelectedPsiElement

class CreateAccessorAction : AnAction() {
    companion object {
        const val ACCESSOR_CLASS = "org.spongepowered.asm.mixin.gen.Accessor"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val element = e.getSelectedPsiElement(PsiField::class.java) ?: return
        val project = e.project ?: return

        val mixin = MixinUtil.findOrCreateAccessor(
            project,
            GlobalSearchScope.projectScope(project),
            PsiTreeUtil.getParentOfType(element, PsiClass::class.java) ?: return
        )

        MixinUtil.findOrAddMixinMethod(
            project,
            mixin,
            $$"$${project.name}$get$${element.name[0].uppercase()}$${element.name.substring(1)}",
            element.typeElement ?: return
        ) { factory, method ->
            if (element.modifierList?.hasExplicitModifier(PsiModifier.STATIC) == true) {
                MixinUtil.injectImplementedByMixinException(project, method)
                method.modifierList.setModifierProperty(PsiModifier.STATIC, true)
            } else {
                method.body?.delete()
            }

            val annotation = factory.createAnnotationFromText("@$ACCESSOR_CLASS(\"${element.name}\")", mixin)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(annotation)
            method.addAnnotation(annotation)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = e.getSelectedPsiElement(PsiField::class.java)?.let { it.modifierList?.hasExplicitModifier(PsiModifier.PUBLIC) != true || it.modifierList?.hasExplicitModifier(PsiModifier.FINAL) == true } ?: false
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}