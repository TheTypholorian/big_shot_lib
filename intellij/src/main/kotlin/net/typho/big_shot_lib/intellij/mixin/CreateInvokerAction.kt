package net.typho.big_shot_lib.intellij.mixin

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement

class CreateInvokerAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val psi = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return
        val element = psi.toUElement() as? UMethod ?: return
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = e.getData(CommonDataKeys.PSI_ELEMENT).toUElement() is UMethod
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}